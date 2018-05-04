# pdfedit
pdf内容替换、也可以修改字体颜色和大小、盖章、插入图片
该项目将网络资源加以整合并优化部分代码，可根据关键字全篇替换PDF文档的内容
原demo code只能成功修改第一页出现的关键字，优化后可修改文档中出现的所有已定义关键字内容

# 引用类库
iText  官网是:http://www.itextpdf.com/
你的pom.xml中需要添加以下依赖
<!-- https://mvnrepository.com/artifact/com.itextpdf/itext-pdfa -->
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itextpdf</artifactId>
    <version>5.5.12</version>
</dependency>
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext-pdfa</artifactId>
    <version>5.5.12</version>
</dependency>
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext-xtra</artifactId>
    <version>5.5.12</version>
</dependency>
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>itext-asian</artifactId>
    <version>5.2.0</version>
</dependency>

# 资源来源
找了半天，没找到原来浏览过的博客。但是项目里面会保留原code作者

