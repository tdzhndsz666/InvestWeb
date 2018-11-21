<?php

    /*
     * 用户注册信息表
     */
    class User extends Zend_Db_Table{
            
        protected $_name='user';//表名
        protected $_primary='id';//主键
        
    }