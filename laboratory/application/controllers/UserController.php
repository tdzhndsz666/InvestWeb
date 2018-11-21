<?php
    
require_once 'BaseController.php';
require_once APPLICATION_PATH.'/models/UserAuths.php';
require_once APPLICATION_PATH.'/models/User.php';
class UserController extends BaseController{
    

    public function init(){
        BaseController::init();//需要调用BaseController类中的init()函数
    }

    /**
     * 用户登录接口
     */
    public function loginAction(){
        
        //获取用户登录信息֤
        $identityType = $this->getRequest()->getParam('identityType');
        $identifier = $this->getRequest()->getParam('identifier');
        $credential = $this->getRequest()->getParam('credential');
        $userAuths = new UserAuths();
        
        $result = $userAuths->fetchRow($userAuths->select()->where('identityType='.$identityType.'and identifier='.$identifier, 1))->toArray();
        
        if(isset($result)){
            //登录成功
            if($credential == $result['credential']){
                $arr = array("successed"=>true,"message"=>"登录成功","content"=>$result);
                exit(json_encode($arr));
            }else{
                //登录失败
                $arr = array("successed"=>false,"message"=>"登录失败");
                exit(json_encode($arr));
            }
            
        }else{
            //登录失败
            $arr = array("successed"=>false,"message"=>"登录失败");
            exit(json_encode($arr));
        }
        
    }
    
    /**
     * 用户注册接口
     */
    public function regAction(){
        
        $json = $this->getRequest()->getParam($key); //json格式数据
        
        $userinfo = json_decode($json);  //转成数组
        
        $user = new User();
        
        $result = $user->insert($userinfo);
        
        if($result){
            
            //注册成功
            $arr = array("successed"=>true,"message"=>"注册成功","content"=>$result);
            exit(json_encode($arr));
            
        }else{
            
            //注册失败
            $arr = array("successed"=>true,"message"=>"注册失败");
            exit(json_encode($arr));
        }
        
    }
}