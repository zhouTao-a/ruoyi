-- 关闭账号自助注册
update sys_config
set config_value = 'false'
where config_key = 'sys.account.registerUser';
