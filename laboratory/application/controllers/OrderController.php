<?php

/**
* 
* @method payAction/getlistAction
* @author lilai
* 
**/
require_once 'BaseController.php';
require_once APPLICATION_PATH.'/models/OrderInfo.php';
require_once APPLICATION_PATH.'/models/OrderDetail.php';

class OrderController extends BaseController{
    

    public function init(){
        BaseController::init();
    }

    /**
     * 购买课程接口
     */
    public function payAction(){
        
    }
    
    /**
     * 根据条件查询课程订单
     */
    public function getlistAction(){

        $userId = $this->getRequest()->getParam("userId");
        $gameId = $this->getRequest()->getParam("gamId");
        
        $orderinfoModel = new OrderInfo();
        //$orderdetailModel = new OrderDetail();
        
        $adapter = $orderinfoModel->getAdapter();
        
        //对orderinfo和orderdetail进行联合查询
        $sql = $adapter->quoteInto(  
            "select distinct *from orderinfo info,orderdetail detail 
            where info.userId=userid 
            and detail.gamId=gamid 
            ",
            array(
                "userid"=>$userId,
                "gamid"=>$gameId
            ));
        
        $result = $adapter->query($sql)->fetchAll();//通过适配器获取的结果已经是数组        
        print "<pre>";
        print_r($result);
        print "<pre>";
        exit();
        if(!empty($result)){           
            $arr = array("queries"=>null,"records"=>$result);
            exit(json_encode($arr));       
        }else{
            $arr = array("queries"=>null,"records"=>null);
            exit(json_encode($arr));
        }  
    }
}