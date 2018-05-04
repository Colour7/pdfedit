package com.color.pdf.pdfsupport;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * 解析PDF中文本的x,y位置
 * @user : caoxu-yiyang@qq.com
 * @user : Color 改进
 * @date : 2017年12月5日
 */
public class PdfPositionParse {

	private PdfReader reader;
	private Map<String,String> replaceText = new HashMap<>();	//�?要查找的文本
	private Map<String,Integer> replaceFontSize = new HashMap<>();	//�?要查找的文本
	private Map<String,BaseColor> replaceFileColor = new HashMap<>();	//�?要查找的文本
	private PdfReaderContentParser parser;
	private Map<Integer,Collection<ReplaceRegion>> pageRegions = new HashMap<>();

	private List<MatchItem> matchItems = new ArrayList<>();//当前页所有文本取出的块内容


	public PdfPositionParse(String fileName) throws IOException{
		FileInputStream in = null;
		try{
			in =new FileInputStream(fileName);
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			init(bytes);
		}finally{
			in.close();
		}
	}
	
	public PdfPositionParse(byte[] bytes) throws IOException{
		init(bytes);
	}
	
	public void addReplaceText(String find,String replace,int fontSize,BaseColor fillColor){
		this.replaceText.put(find,replace);
		this.replaceFontSize.put(find, fontSize);
		this.replaceFileColor.put(find, fillColor);
	}
	
	private void init(byte[] bytes) throws IOException {
		reader = new PdfReader(bytes);
		parser = new PdfReaderContentParser(reader);
	}
	
	/**
	 * 解析文本
	 * @throws IOException
	 */
	public void parse() throws IOException{
		try{
			if(this.replaceText.isEmpty()){
				throw new NullPointerException("没有找到要查找的文本");
			}
			int total = reader.getNumberOfPages();
			for(int i = 0 ; i < total ; i++) {
				PositionRenderListener listener = new PositionRenderListener(replaceText,replaceFontSize,replaceFileColor);
				//findMatchWordItems(listener);
				int page = i + 1;
				parser.processContent(page, listener);
				findMatchWordItems(listener);
				//this.pageRegions.put(page, listener.getResult());
				this.pageRegions.put(page, listener.getResult().values());
			}
			
		}finally{
			if(reader != null){
				reader.close();
			}
		}
	}

	/**
	 * @return the pageRegions
	 */
	public Map<Integer,Collection<ReplaceRegion>> getPageRegions() {
		return pageRegions;
	}

	public List<MatchItem> getMatchItems() {
		return matchItems;
	}

	/**
	 * 找到匹配的关键词块
	 * @param renderListener
	 * @return
	 */
	public void findMatchWordItems(PositionRenderListener renderListener){
		Set<Map.Entry<String, String>> entrys = replaceText.entrySet();

		//先判断本页中是否存在关键词
		List<MatchItem> allItems = renderListener.getAllItems();//所有块LIST
		StringBuffer sbtemp = new StringBuffer("");
		for(MatchItem item : allItems){//将一页中所有的块内容连接起来组成一个字符串。
//			sbtemp.append(item);
			//第一种情况：关键词与块内容完全匹配的项
			//第三种情况：关键词存在块中
			for (Map.Entry<String, String> entry : entrys) {
				String key = entry.getKey();
				String value = entry.getValue();
				if(!StringUtils.isEmpty(item.getContent()) && key.equals(item.getContent())) {
					item.setNewContent(value);
					matchItems.add(item);
				}else if(!StringUtils.isEmpty(item.getContent()) && item.getContent().indexOf(key) != -1) {
					item.setNewContent(value);
					matchItems.add(item);
				}
			}
		}

		//第二种情况：多个块内容拼成一个关键词，则一个一个来匹配，组装成一个关键词
		//1，关键词中存在某块 2，拼装的连续的块=关键词 3，避开某个块完全匹配关键词
		//关键词 中国移动 而块为 中 ，国，移动
		//关键词 中华人民 而块为中，华人民共和国 这种情况解决不了，也不允许存在
		List<MatchItem> tempItems = null;
		for (Map.Entry<String, String> entry : entrys){
			String keyword = entry.getKey();
			String value = entry.getValue();
			sbtemp = new StringBuffer("");
			tempItems = new ArrayList();
			for(MatchItem item : allItems){
				if(keyword.indexOf(item.getContent()) != -1 && !keyword.equals(item.getContent())){
					tempItems.add(item);
					sbtemp.append(item.getContent());
					if(keyword.indexOf(sbtemp.toString()) == -1){//如果暂存的字符串和关键词 不再匹配时
						sbtemp = new StringBuffer(item.getContent());
						tempItems.clear();
						tempItems.add(item);
					}
					if(sbtemp.toString().equals(keyword)){//暂存的字符串正好匹配到关键词时
						MatchItem tmpitem = getRightItem(tempItems, keyword,value);
						if(tmpitem != null){
							matchItems.add(tmpitem);//得到匹配的项
						}
						sbtemp = new StringBuffer("");//清空暂存的字符串
						tempItems.clear();//清空暂存的LIST
						continue;//继续查找
					}
				}else{//如果找不到则清空
					sbtemp = new StringBuffer("");
					tempItems.clear();
				}
			}
		}

		if (CollectionUtils.isNotEmpty(this.matchItems)){
			Map<String, ReplaceRegion> result = (Map<String, ReplaceRegion>) renderListener.getResult();
			for (MatchItem matchItem : matchItems){
				String keyString = matchItem.getContent();
				ReplaceRegion replaceRegion = result.get(keyString);
				if(replaceRegion == null) {
					replaceRegion = new ReplaceRegion();

				}
				int fontSize = this.replaceFontSize.get(keyString) == null ?0 : this.replaceFontSize.get(keyString);
				replaceRegion.addRegion(matchItem.getX(), matchItem.getY(), matchItem.getPageWidth(),
						matchItem.getPageHeight(),this.replaceFileColor.get(keyString),
						fontSize,matchItem.getNewContent());
				result.put(keyString, replaceRegion);
			}
		}
		/*if(replacement != null) {
			String keyString = replacement.getKeyString();
			ReplaceRegion replaceRegion = result.get(keyString);
			if(replaceRegion == null) {
				replaceRegion = new ReplaceRegion();
				result.put(keyString, replaceRegion);
			}
			int fontSize = this.replaceFontSize.get(keyString) == null ?0 : this.replaceFontSize.get(keyString);
			replaceRegion.addRegion(bound.x, bound.y-this.fixHeight, bound.width, bound.height == 0 ? defaultH : bound.height,this.replaceFileColor.get(keyString),fontSize,replacement.getNewText());
		}*/

		return;
	}

	/**
	 *  返回新的chuck
	 * @param tempItems  size()大于1的文本块
	 * @param keyword  需要替换的内容
	 * @param newValue  替换后的内容
	 * @return
	 */
	public MatchItem getRightItem(List<MatchItem> tempItems,String keyword, String newValue){
		if(CollectionUtils.isNotEmpty(tempItems) && !StringUtils.isEmpty(keyword)){
			MatchItem newItem = new MatchItem();
			int size = tempItems.size();
			//具体替换时  是根据坐标进行替换的，所以这里的keyword是我们最初设置写入的keyword；实际被替换的内容是tempItems的所有文本的拼接结果
			newItem.setContent(keyword);

			MatchItem firstItem = tempItems.get(0);
			MatchItem lastItem = tempItems.get(size -1);

			newItem.setX(firstItem.getX());
			newItem.setY(lastItem.getY());
			newItem.setPageWidth(lastItem.getX() + lastItem.getPageWidth() - firstItem.getX());
			newItem.setPageHeight(firstItem.getPageHeight() + firstItem.getY() - lastItem.getY());
			newItem.setNewContent(newValue);
			return newItem;
		}
		return null;
	}

}
