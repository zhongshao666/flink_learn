package dp.decorator;

import java.util.HashMap;

public class DemoMain {
    public static void main(String[] args){
        Circle circle = new Circle();
        RedShapeDecorator redCircle = new RedShapeDecorator(new Circle());
        RedShapeDecorator redRectangle = new RedShapeDecorator(new Rectangle());

        Shape redCircle1 = new RedShapeDecorator(new Circle());
        Shape redRectangle1 = new RedShapeDecorator(new Rectangle());

        System.out.println("Circle with normal border");
        circle.draw();

        System.out.println("\nCircle with red border");
        redCircle.draw();
        redCircle1.draw();

        System.out.println("\nRectangle with red border");
        redRectangle.draw();
        redRectangle1.draw();
        new HashMap<>();
    }
}
