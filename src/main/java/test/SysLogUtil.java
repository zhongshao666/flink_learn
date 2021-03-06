package test;

import org.graylog2.syslog4j.Syslog;
import org.graylog2.syslog4j.SyslogIF;
import org.graylog2.syslog4j.impl.net.tcp.TCPNetSyslogConfig;
import org.graylog2.syslog4j.impl.net.udp.UDPNetSyslogConfig;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.URLDecoder;
import java.util.Date;

public class SysLogUtil {

    public static void main(String[] args) throws Throwable {



        SysLogUtil log2 = new SysLogUtil();

            StringBuffer buffer = new StringBuffer();
            buffer.append("time：").append(new Date().toString().substring(4, 20)).append(";");
            buffer.append("userID:" + "uuu1" + ";");
            buffer.append("logType:" + "100" + ";");
            buffer.append("actiom:" + "delete" + ";");
            buffer.append("des:").append(System.currentTimeMillis());


//            sendLog("192.168.18.233", 443, buffer.toString(), 18);
//            sendLog("192.168.18.74", 50443, buffer.toString(), 18);

//        sendLog("127.0.0.1", 19002, buffer.toString(), 1);
            sendLog("220.184.49.69", 50448, buffer.toString(), 18);

//        log2.sendLog("172.18.10.124", 514, buffer.toString(), 1);
//        test();

    }

    public static void test() throws Throwable {
        // TODO Auto-generated method stub
        DatagramSocket datagramSocket = new DatagramSocket(5144);
        while (true) {
            DatagramPacket packet = new DatagramPacket(new byte[512], 512);
            try {
                //不会造成死循环，因为receive是阻塞式方法，若发送方不发送数据，则阻塞在该处
                datagramSocket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());

                System.out.println(packet.getAddress() + "/" + packet.getPort() + ":" + msg);
                packet.setData("I am server!!!".getBytes());
                datagramSocket.send(packet);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }



    /**
     * 发送syslog
     *
     * @param host
     * @param port
     * @param log
     * @param level
     */
    public static void sendLog(String host, int port, String log, int level) {
        try {

            UDPNetSyslogConfig config = new UDPNetSyslogConfig();
//设置syslog服务器端地址
            config.setHost(host);
//设置syslog接收端口，默认514
            config.setPort(port);
//向多个多个ip发送日志不执行shutdo会导致同一个实例无法发送到多个地址
            Syslog.shutdown();
//获取syslog的操作类，使用udp协议。syslog支持"udp", "tcp", "unix_syslog", "unix_socket"协议
            SyslogIF syslog = Syslog.createInstance("udp", config);
            syslog.log(level, URLDecoder.decode(log, "utf-8"));
            System.out.println( URLDecoder.decode(log, "utf-8"));
            System.out.println("syslog Server:" + host + ":" + 50448);
/* 发送信息到服务器，2表示日志级别 范围为0~7的数字编码，表示了事件的严重程度。0最高，7最低
* syslog为每个事件赋予几个不同的优先级：
0 LOG_EMERG：紧急情况，需要立即通知技术人员。
1 LOG_ALERT：应该被立即改正的问题，如系统数据库被破坏，ISP连接丢失。
2 LOG_CRIT：重要情况，如硬盘错误，备用连接丢失。
3 LOG_ERR：错误，不是非常紧急，在一定时间内修复即可。
4 LOG_WARNING：警告信息，不是错误，比如系统磁盘使用了85%等。
5 LOG_NOTICE：不是错误情况，也不需要立即处理。
6 LOG_INFO：情报信息，正常的系统消息，比如骚扰报告，带宽数据等，不需要处理。
7 LOG_DEBUG：包含详细的开发情报的信息，通常只在调试一个程序时使用。
*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}