create table user_info
(
  id             int                      not null,
  name           varchar(10) charset utf8 not null,
  gender         varchar(10) charset utf8 null,
  age            int                      null,
  phone          varchar(20)              null,
  register_mode  varchar(20)              null comment '注册方式，例如手机注册，微信注册等',
  third_party_id varchar(64)              null comment '第三方id，例如微信的id',
  constraint user_info_id_uindex
    unique (id)
);

alter table user_info
  add primary key (id);


-- auto-generated definition
create table user_password
(
  id               int          not null
    primary key,
  encrypt_password varchar(128) not null,
  user_id          int          not null
);
