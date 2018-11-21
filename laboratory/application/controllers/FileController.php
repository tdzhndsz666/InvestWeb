<?php

/**
 * 功能：文件上传下载接口类
 */
require_once 'BaseController.php';
require_once APPLICATION_PATH.'/controllers/commontool/File.php';
class FileController extends BaseController{

    public function init(){
        BaseController::init();
    }

    
    /**
     * 文件上传接口
     */
    public function fileuploadAction(){
        
        //获取文件信息
        $fileInfo = $_FILES["file"];
        //$fileInfo = "D:\aaa.txt";

        $file = new File();
        $result = $file->fileUpload($fileInfo);
        if($result == 1){
            $arr = array("successed"=>true,"message"=>"上传成功");
            exit($arr);           
        }
        else if($result == 2){
            $arr = array("successed"=>false,"message"=>"文件已存在");
            exit($arr); 
        }
        else if($result == 0){
            $arr = array("successed"=>false,"message"=>"上传失败");
            exit($arr);
        }
    }
    
    /**
     * 课程文件下载接口
     */
    public function downloadAction(){
        
        $url = $this->getRequest()->getParam("url");
        
        $arr = array("successed"=>false,"message"=>"登录失败");
        exit(json_encode($arr));
    }
    
    /**
     * 音频/视频下载接口
     */
    public function downaudioAction(){
        
    }
    
}