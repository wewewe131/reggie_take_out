package com.reggie.Controller;

import com.reggie.Common.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggieFile.uploadPath}")
    private String uploadPath;

    /**
     * 文件上传
     *
     * @param file 参数名需要跟前端保持一致
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        //收到请求后会生成一个临时文件,在请求中需要将文件转存，否则在请求结束后临时文件会删除
        log.info(file.toString());

        //获取原文件名
        String fileOriginName = file.getOriginalFilename();

        //查找最后一个"."出现的位置,substring截取从最后出现的位置到末尾的字符串，此处是为了取出文件后缀
        String suffix = fileOriginName.substring(fileOriginName.lastIndexOf("."));

        //随机生成UUID将文件重命名,避免出现文件同名的情况
        String newFileName = UUID.randomUUID().toString() + suffix;

        //根据路径新建一个对象
        File dir = new File(uploadPath);

        //判断路径是否存在
        if (!dir.exists())
            dir.mkdir();//如果不存在就新建一个目录

        try {
            //将文件转存到指定的目录下
            file.transferTo(new File(uploadPath + newFileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return R.success(newFileName);
    }

    //文件下载

    @GetMapping("/download")
    public void download(@RequestParam("name") String fileName, HttpServletResponse response, HttpServletRequest request) {
        log.info("request Content-Type:" + request.getContentType());
        //根据文件名查找文件

        //通过输入流读取文件内容 参数1：文件路径  参数2：文件名
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(uploadPath + fileName));
        } catch (FileNotFoundException e) {
            try {
                fileInputStream = new FileInputStream(new File(uploadPath + "notfound.png"));
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
        //使用SpringBoot自带的工具类 通过文件后缀获取ContentType
        String contentType = MediaTypeFactory.getMediaType(fileName).map(x -> x.toString()).orElse("");
        //设置响应头
        response.setContentType(contentType);
        //输出流,输出流文件到浏览器
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();

            int len = 0;
            //设置每次循环读取的大小，每次读取 1024bit 的内容，如果文件大于 1024bit 就在下一次再读取 1024bit 知道读完为止
            byte[] bytes = new byte[1024];
//            byte[] bytes = new byte[1024*1024*30];
            //循环
            while (
                    (       //从文件中读取bytes.length字节的数据到bytes中
                            len = fileInputStream.read(bytes)
                    )
                            != -1
            ) {
                //先计算off设置的偏移位置，再从下标为len的位置开始把bytes中缓存的数据写入流中
                outputStream.write(bytes, 0, len);
                //刷新输出流，并且将流中已存在的数据强制重新写入
                outputStream.flush();

            }
            //关闭输出流
            outputStream.close();
            //关闭文件输入流
            fileInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
