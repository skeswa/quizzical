docker run -d \
  -p 4001:4001 \
  -p 7001:7001 \
  -v /var/etcd/:/data \
  mduduzik/conxsoft-etcd \
  --name conx-etcd

docker run -d  --name conx-etcd-dev -p 4001:4001   -p 7001:7001   -v /var/etcd/:/data conxadmin/etcd -name conx-etcd-dev
