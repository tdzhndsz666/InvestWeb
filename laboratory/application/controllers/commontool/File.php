<?php

/**
 * 功能：文件操作类
 * @author w
 *
 */
class File
{
    /**
     * 功能：文件上传
     * @param  $arr：$_FILES["file"]上传文件信息
     * @return 0:文件不符合规定  1：文件上传成功  2：上传文件已经存在
     */
    public function fileUpload($arr){
        
        /*加限制条件
         * 1.文件类型
         * 2.文件大小
         * 3.保存的文件名不重复
         */
        if(($arr["type"]=="txt") && $arr["size"]<10241000 )
        {
            
            /* 上传的文件存放的位置
             * 避免文件重复:
             * 1.加时间戳.time()加用户名.$uid或者加.date('YmdHis')
             * 2.类似网盘，使用文件夹来防止重复
             */
            $filename = "./gamefile/".date('YmdHis').$arr["name"];
            //保存之前判断该文件是否存在
            if(file_exists($filename))
            {
                return 2;
            }
            else
            {
                //中文名的文件出现问题，所以需要转换编码格式
                $filename = iconv("UTF-8","gb2312",$filename);
                
                /* 移动临时文件到上传的文件存放的位置
                 * 括号里：1.临时文件的路径
                 *         2.存放的路径
                 */
                move_uploaded_file($arr["tmp_name"],$filename);
                return 1;
            }
        }
        else
        {
            return 0;
        }
    }
    public function fileDownload($filePath, $downloadFileName = null){
        
        if (file_exists($filePath)) {
            $downloadFileName = $downloadFileName !== null ? $downloadFileName : basename($filePath);
            header('Content-Description: File Transfer');
            header('Content-Type: application/octet-stream');
            header('Content-Disposition: attachment; filename=' . $downloadFileName);
            header('Content-Transfer-Encoding: binary');
            header('Expires: 0');
            header('Cache-Control: must-revalidate, post-check=0, pre-check=0');
            header('Pragma: public');
            header('Content-Length: '.filesize($filePath));
            ob_clean();//清空php的缓冲区
            flush();//清空web服务器的缓存区php的缓冲区超过限制php脚本还没有结束就会输出到服务器的缓存区或者浏览器的缓冲区  清除他？
            readfile($filePath);
            exit;
        }else{
            redirect($_SERVER['HTTP_REFERER']);//看业务逻辑  这里是如果文件不存在  避免调到空白页面
        }
    }
}