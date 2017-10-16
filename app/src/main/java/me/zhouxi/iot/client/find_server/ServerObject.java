package me.zhouxi.iot.client.find_server;

/**
 * Created by zhouxi on 8/10/2017.
 */

public class ServerObject {

    public String ip;

    public int port;

    public String version;

    public String name;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ServerObject){
            ServerObject serverObject = (ServerObject) obj;
            boolean b1 = serverObject.ip.equals(this.ip);
            boolean b2 = serverObject.port == this.port;
            boolean b3 = serverObject.version.equals(this.version);
            boolean b4 = serverObject.name.equals(this.name);
            return b1 && b2 && b3 && b4;
        }else {
            return false;
        }
    }
}
