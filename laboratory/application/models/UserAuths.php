<?php

    /*
     * 用户登录信息表
     */
    class UserAuths extends Zend_Db_Table{
            
        protected $_name='userauths';//表名
        protected $_primary='id';//指定主键,默认是id
        
    }