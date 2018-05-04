package com.color.pdf.util;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.color.pdf.constant.ErrorCodeMapping;
import com.color.pdf.model.GlobalRequestResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

/**
 * Created by lixiao on 2017/5/23.
 */
@Slf4j
public class FileUtil {


    /**
     * 读取文件路径
     * @param fileName
     * @return
     * @throws IOException
     */
    public static URL loadFile(String fileName) throws IOException {
        ClassLoader classLoader = ExtensionLoader.class.getClassLoader();

        URL url = classLoader.getResource(fileName);
        if (url != null) {
            return url;
        } else {
            return null;
        }
    }


    public static GlobalRequestResult fileUploadExcel(MultipartFile fileUpload, HttpServletRequest request) {
        try {
            String fileName = fileUpload.getOriginalFilename();
            String[] suffixs = fileName.split("\\.");
            String suffix = suffixs[suffixs.length - 1];
            String suffixLc = suffix.toLowerCase();
            if (suffixLc.equals("xls") || suffixLc.equals("xlsx")) {
                byte[] bytes = fileUpload.getBytes();
                Long fileSize = fileUpload.getSize();
                // 5M
                if (fileSize <= 5 * 1024 * 1024) {
                    UUID uuid = UUID.randomUUID();
                    String uuidStr = uuid.toString().replace("-", "");
                    String uploadFilePath = "/upload/excel/" + uuidStr + "." + suffixLc;
                    ServletContext servletContext = request.getSession().getServletContext();
                    String path = servletContext.getRealPath(uploadFilePath);
                    File fileObj = new File(path);
                    FileCopyUtils.copy(bytes, fileObj);
                    return GlobalRequestResult.wrapSuccessResult(path);
                } else {
                    return GlobalRequestResult.wrapErrorResult(ErrorCodeMapping.PARAM_EXCEPTION, "文件大小超过5M，文件内容可能包含其它格式，或进行多次导入");
                }
            } else {
                return GlobalRequestResult.wrapErrorResult(ErrorCodeMapping.PARAM_EXCEPTION, "文件类型错误，只支持xls或者xlsx文件");
            }
        } catch (Exception e) {
            log.error("[上传文件错误]", e);
            return GlobalRequestResult.wrapErrorResult(ErrorCodeMapping.SERVICES_EXCEPTION, "未知错误");
        }
    }

    /**
     * 生成一个带后缀的随机文件的临时路径
     *
     * @param suffix 文件后缀
     * @return 临时文件路径
     */
    public static final String createTemporaryFilePath(String suffix) {
        StringBuilder path = new StringBuilder();
        path.append(getTempDirectory())
                .append(getFileSeparator())
                .append(uuid());

        if (!StringUtils.isEmpty(suffix)) {
            path.append(".").append(suffix);
        }
        log.info("新文件路径："+path.toString());
        return path.toString();
    }

    /**
     * 获取IO的临时文件目录
     *
     * @return 本应用的临时目录
     */
    public final static String getTempDirectory() {
        return System.getProperty("java.io.tmpdir");
    }

    public static final String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    public static final String uuid(){
        return UUID.randomUUID().toString();
    }


}
