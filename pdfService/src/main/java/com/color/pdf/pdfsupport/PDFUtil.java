package com.color.pdf.pdfsupport;

import com.alibaba.fastjson.JSON;
import com.color.pdf.util.FileUtil;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 * @author llade 改进
 * @author color 改进
 *
 */
@Slf4j
public class PDFUtil {

	/**
	 * 斜角排列、全屏多个重复的花式文字水印
	 * 
	 * @param input
	 *			需要加水印的PDF读取输入流
	 * @param output
	 *			输出生成PDF的输出流
	 * @param waterMarkString
	 *			水印字符
	 * @param xAmout
	 *			x轴重复数量
	 * @param yAmout
	 *			y轴重复数量
	 * @param opacity
	 *			水印透明度
	 * @param rotation
	 *			水印文字旋转角度，一般为45度角
	 * @param waterMarkFontSize
	 *			水印字体大小
	 * @param color
	 *			水印字体颜色
	 */
	public static void stringWaterMark(InputStream input, OutputStream output, String waterMarkString, int xAmout,
			int yAmout, float opacity, float rotation ,int waterMarkFontSize, BaseColor color) {
		try {

			PdfReader reader = new PdfReader(input);
			PdfStamper stamper = new PdfStamper(reader, output);

			// 添加中文字体
			BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			
			int total = reader.getNumberOfPages() + 1;

			PdfContentByte over;
			// 给每一页加水印
			for (int i = 1; i < total; i++) {
				Rectangle pageRect = stamper.getReader().getPageSizeWithRotation(i);
				// 计算水印每个单位步长X,Y
				float x = pageRect.getWidth() / xAmout;
				float y = pageRect.getHeight() / yAmout;

				over = stamper.getOverContent(i);
				PdfGState gs = new PdfGState();
				// 设置透明度为
				gs.setFillOpacity(opacity);
	
				over.setGState(gs);
				over.saveState();

				over.beginText();
				over.setColorFill(color);
				over.setFontAndSize(baseFont, waterMarkFontSize);

				for (int n = 0; n < xAmout + 1; n++) {
					for (int m = 0; m < yAmout + 1; m++) {
						over.showTextAligned(Element.ALIGN_CENTER, waterMarkString, x * n, y * m, rotation);
					}
				}

				over.endText();
			}
			stamper.close();
		} catch (Exception e) {
			log.error("水印制作错误{}", JSON.toJSONString(e));
		}
	}

	/**
	 * 图片水印，整张页面平铺
	 * 
	 * @param input
	 *            需要加水印的PDF读取输入流
	 * @param output
	 *            输出生成PDF的输出流
	 * @param imageFile
	 *            水印图片路径
	 */
	public static void imageWaterMark(InputStream input, OutputStream output, String imageFile, float opacity) {
		try {

			PdfReader reader = new PdfReader(input);
			PdfStamper stamper = new PdfStamper(reader, output);
			Rectangle pageRect = stamper.getReader().getPageSize(1);
			float w = pageRect.getWidth() ;
			float h = pageRect.getHeight() ;

			int total = reader.getNumberOfPages() + 1;

			Image image = Image.getInstance(imageFile);
			image.setAbsolutePosition(0, 0);// 坐标
			image.scaleAbsolute(w, h);

			PdfGState gs = new PdfGState();
			gs.setFillOpacity(opacity);// 设置透明度

			PdfContentByte over;
			// 给每一页加水印
			for (int i = 1; i < total; i++) {
				over = stamper.getOverContent(i);
				over.setGState(gs);
				over.saveState();//没这个的话，图片透明度不起作用,必须在beginText之前，否则透明度不起作用，会被图片覆盖了内容而看不到文字了。
				over.beginText();
				// 添加水印图片
				over.addImage(image);
			}
			stamper.close();
		} catch (Exception e) {
			log.error("水印制作错误{}", JSON.toJSONString(e));
		}
	}

	/**
	 * pdf文本替换
	 * 字体默认10
	 * 如需添加背景色||自定义字体，请重写该方法
	 *
	 * @param input
	 * 			需要替换的PDF读取输入流
	 * @param replaceMap
	 * 			需要替换的key-value
	 * @return
	 * 		    File文件流
	 */
	public static File replaceText(InputStream input, Map<String,String> replaceMap){
		log.info("-------------replaceMap=" + JSON.toJSONString(replaceMap));
		if(replaceMap == null || replaceMap.size() <= 0){
			log.error("没有传入需要替换的文本{}",JSON.toJSONString(replaceMap));
			return null;
		}
		if(input == null){
			log.error("调用pdf替换文本方法时传入的File是空的");
			return null;
		}
		try {
			PdfReplacer textReplacer = new PdfReplacer(input);
			for (String keyword : replaceMap.keySet()){
				textReplacer.replaceText(keyword,replaceMap.get(keyword));
			}
			File file = new File( FileUtil.createTemporaryFilePath("pdf"));//临时文件
			if(!file.exists()&&file.isDirectory()){//判断文件目录是否存在
				file.mkdirs();
			}
			textReplacer.toPdf(file);
			return file;
		} catch (Exception e) {
			log.error("替换pdf文本方法-解析文本异常",e);
		}
		return null;
	}

	/**
	 * pdf文本替换
	 * 字体默认10
	 * 如需添加背景色||自定义字体，请重写该方法
	 *
	 * @param pdfBytes
	 * 			需要替换的PDF读取输入流
	 * @param replaceMap
	 * 			需要替换的key-value
	 * @return
	 * 		    File文件流
	 */
	public static File replaceText(byte[] pdfBytes, Map<String,String> replaceMap){
		log.info("-------------replaceMap=" + JSON.toJSONString(replaceMap));
		if(replaceMap == null || replaceMap.size() <= 0){
			log.error("没有传入需要替换的文本{}",JSON.toJSONString(replaceMap));
			return null;
		}
		try {
			PdfReplacer textReplacer = new PdfReplacer(pdfBytes);
			for (String keyword : replaceMap.keySet()){
				textReplacer.replaceText(keyword,replaceMap.get(keyword));
			}
			File file = new File( FileUtil.createTemporaryFilePath("pdf"));//临时文件
			if(!file.exists()&&file.isDirectory()){//判断文件目录是否存在
				file.mkdirs();
			}
			textReplacer.toPdf(file);
			return file;
		} catch (Exception e) {
			log.error("替换pdf文本方法-解析文本异常",e);
		}
		return null;
	}

	/**
	 * pdf文本替换
	 * 字体默认10
	 * 如需添加背景色||自定义字体，请重写该方法
	 *
	 * @param input
	 * 			需要替换的PDF读取输入流
	 * @param replaceMap
	 * 			需要替换的key-value
	 * @return
	 * 		    文件字节流
	 */
	public static byte[] replaceTextAndToByte(InputStream input, Map<String,String> replaceMap){
		log.info("-------------replaceMap=" + JSON.toJSONString(replaceMap));
		if(replaceMap == null || replaceMap.size() <= 0){
			log.error("没有传入需要替换的文本{}",JSON.toJSONString(replaceMap));
			return null;
		}
		if(input == null){
			log.error("调用pdf替换文本方法时传入的File是空的");
			return null;
		}
		try {
			PdfReplacer textReplacer = new PdfReplacer(input);
			for (String keyword : replaceMap.keySet()){
				textReplacer.replaceText(keyword,replaceMap.get(keyword));
			}
			byte[] bytes = textReplacer.toBytes();
			return bytes;
		} catch (Exception e) {
			log.error("替换pdf文本方法-解析文本异常",e);
		}
		return null;
	}

	/**
	 * pdf文本替换
	 * 字体默认10
	 * 如需添加背景色||自定义字体，请重写该方法
	 *
	 * @param pdfBytes
	 * 			需要替换的PDF字节流
	 * @param replaceMap
	 * 			需要替换的key-value
	 * @return
	 * 		    文件字节流
	 */
	public static byte[] replaceTextAndToByte(byte[] pdfBytes, Map<String,String> replaceMap){
		log.info("-------------replaceMap=" + JSON.toJSONString(replaceMap));
		if(replaceMap == null || replaceMap.size() <= 0){
			log.error("没有传入需要替换的文本{}",JSON.toJSONString(replaceMap));
			return null;
		}
		try {
			PdfReplacer textReplacer = new PdfReplacer(pdfBytes);
			for (String keyword : replaceMap.keySet()){
				log.info("pdf文本替换[replaceTextAndToByte]：{keyword=" + keyword + ",value=" + replaceMap.get(keyword) +"}");
				textReplacer.replaceText(keyword,replaceMap.get(keyword));
			}
			byte[] bytes = textReplacer.toBytes();
			return bytes;
		} catch (Exception e) {
			log.error("替换pdf文本方法-解析文本异常",e);
		}
		return null;
	}


}