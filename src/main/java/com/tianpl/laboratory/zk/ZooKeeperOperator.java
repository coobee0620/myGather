package com.tianpl.laboratory.zk;

import com.tianpl.laboratory.zk.domain.NodeCreateReq;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

import java.util.List;

/**
 * Created by tianyu on 16/12/1.
 */
public class ZooKeeperOperator extends AbstractZooKeeper {

    public void create(NodeCreateReq req) throws KeeperException, InterruptedException {
        create(req.getPath(),req.getData(),req.isPersistent(),req.isSequential());
    }

    private void create(String path,byte[] data,boolean persistent,boolean sequential) throws KeeperException, InterruptedException {
        CreateMode createMode;
        if (persistent) {
            if (sequential) {
                createMode = CreateMode.PERSISTENT_SEQUENTIAL;
            } else {
                createMode = CreateMode.PERSISTENT;
            }
        } else {
            if (sequential) {
                createMode = CreateMode.EPHEMERAL_SEQUENTIAL;
            } else {
                createMode = CreateMode.EPHEMERAL;
            }
        }
        /**
         * 此处采用的是CreateMode是PERSISTENT  表示The znode will not be automatically deleted upon client's disconnect.
         * EPHEMERAL 表示The znode will be deleted upon the client's disconnect.
         */
        this.zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
    }

    public byte[] getData(String path) throws KeeperException, InterruptedException {
        return  this.zooKeeper.getData(path, false,null);
    }

    public void delete(String path) throws KeeperException, InterruptedException {
        this.zooKeeper.delete(path,-1);
    }

    public void getChild(String path) throws KeeperException, InterruptedException{
        try{
            List<String> list=this.zooKeeper.getChildren(path, false);
            if(list.isEmpty()){
                System.out.println(path+"中没有节点");
            }else{
                System.out.println(path+"中存在节点");
                for(String child:list){
                    System.out.println("节点为："+child);
                }
            }
        }catch (KeeperException.NoNodeException e) {
            // TODO: handle exception
            throw e;

        }
    }

    public static void main(String[] args) {
        try {
            ZooKeeperOperator zkoperator             = new ZooKeeperOperator();
            zkoperator.connect("192.168.50.202,192.168.50.203,192.168.50.204");

//              zkoperator.create("/root",null);
//              System.out.println(Arrays.toString(zkoperator.getData("/root")));
//
//              zkoperator.create("/root/child1",data);
//              System.out.println(Arrays.toString(zkoperator.getData("/root/child1")));
//
//              zkoperator.create("/root/child2",data);
//              System.out.println(Arrays.toString(zkoperator.getData("/root/child2")));

            zkoperator.delete("/home/mtime/ty");
            String zktest="ZooKeeper的Java API测试";
            NodeCreateReq req = new NodeCreateReq();

            req.setData(zktest.getBytes());
            req.setPath("/home/mtime/ty");
            req.setPersistent(true);
            zkoperator.create(req);

            req.setPersistent(false);
            req.setData("123".getBytes());
            req.setPath("/home/mtime/ty/1");
            zkoperator.create(req);
            System.out.println("获取设置的信息："+new String(zkoperator.getData("/home/mtime/ty")));

            System.out.println("节点孩子信息:");
            zkoperator.getChild("/home");

            zkoperator.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
