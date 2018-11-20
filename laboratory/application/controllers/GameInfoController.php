<?php

/**
 * 
 */
require_once 'BaseController.php';
require_once APPLICATION_PATH.'/models/GameInfo.php';

class GameInfoController extends BaseController{
    
    
    public function init(){
        BaseController::init();
    }

    public function listAction(){
        
        $startRowIndex = $this->getRequest()->getParam('startRowIndex');
        $pageSize = $this->getRequest()->getParam('pageSize');
        $orderBy = $this->getRequest()->getParam('orderBy');
        $isOrderAsc = $this->getRequest()->getParam('isOrderAsc');
        
        $gameInfo = new GameInfo();
        
        $result = $gameInfo->fetchAll(
            null,$orderBy,$pageSize,$startRowIndex)->toArray();
            //$result = $gameInfo->fetchAll()->toArray();
            
            if(!empty($result)){
                //获取成功
                $arr = array("queries"=>true,"records"=>$result,"totalRecords"=>count($result));
                exit(json_encode($arr));
                
            }else{
                //获取失败
                $arr = array("queries"=>null,"records"=>null,"totalRecords"=>0);
                exit(json_encode($arr));
                
            }
    }
}