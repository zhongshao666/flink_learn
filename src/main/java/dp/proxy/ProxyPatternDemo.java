package dp.proxy;

public class ProxyPatternDemo {
    public static void main(String[] args){
        Image image = new ProxyImage("demo_666.jpg");
        //图片从磁盘加载
        image.display();
        System.out.println();
        //图片不需要从磁盘加载
        image.display();
    }
}
