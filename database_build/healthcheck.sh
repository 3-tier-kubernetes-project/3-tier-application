#!/bin/bash
res=$(mysql --user=root --password=$MYSQL_ROOT_PASSWORD -se "SELECT 1 FROM DUAL;");
if [ 1 -eq $res ]
then
  exit 0
else
  exit 1
fi

