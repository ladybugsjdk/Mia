package finalproject.Mia;

import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends JFrame
{
    private boolean isTurn;
    private final int lives = 6;
    private final int score = 0;
    private int playerNumber;

    public static void main(String[] args)
    {
        Client client1 = new Client();
        client1.setPlayerNumber(1);
        client1.setTurn(true);
        client1.loadTitleScreen();
    }

    public Client()
    {

    }

    public void connectToServer()
    {
        try
        {
            //get address of client machine
            InetAddress inetAddress = InetAddress.getLocalHost();
            String clientAddress = inetAddress.getHostAddress();
            System.out.println("Client Address: " + clientAddress);

            String server;
            server = "localhost";

            //create socket to connect to server address on port 4999
            Socket socket = new Socket(server, 4999);

            //open stream to write from file testInput1.txt (not included)
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());

            String jsonMessage = "{\"playerNumber\":\"" + getPlayerNumber() + "\", \"isTurn\":\"" + isTurn() +
                    "\", \"lives\":\"" + getLives() + "\", \"score\":\"" + getScore() + "\"}";

            //send entire message to server and close stream
            System.out.println("\n\nSending to server: \n" + jsonMessage);
            printWriter.println(jsonMessage);
            printWriter.flush();
            printWriter.close();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void loadTitleScreen()
    {
        setTitle("Mia - " + getPlayerNumber());
        JPanel jPanel = new JPanel();
        initialize(jPanel);
        getContentPane().add(jPanel, BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    public void initialize(JPanel jPanel)
    {
        jPanel.setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        canvas3D.setSize(400, 400);
        SimpleUniverse universe = new SimpleUniverse(canvas3D);
        BranchGroup branchGroup = new BranchGroup();
        addText(branchGroup);
        addLights(branchGroup);
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(branchGroup);
        jPanel.add(canvas3D, BorderLayout.CENTER);

        JButton startBtn = new JButton("Start Game");
        startBtn.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent m)
            {
                connectToServer();
            }

        });
        jPanel.add(startBtn, BorderLayout.SOUTH);
    }

    public void addLights(BranchGroup branchGroup) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                1000.0);
        Color3f light1Color = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light1 = new DirectionalLight(light1Color,
                light1Direction);
        light1.setInfluencingBounds(bounds);
        branchGroup.addChild(light1);
        Color3f ambientColor = new Color3f(.1f, .1f, .1f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        branchGroup.addChild(ambientLightNode);
        Background back = new Background(1, 1, 1);
        back.setApplicationBounds(bounds);
        branchGroup.addChild(back);
    }

    private void addText(BranchGroup branchGroup) {
        Font3D font3D = new Font3D(new Font("Arial", Font.BOLD, 1),
                new FontExtrusion());

        Text3D firstName = new Text3D(font3D, "Welcome to", new Point3f(-2f, 2f, -4.8f));
        firstName.setString("Welcome to");

        Text3D lastName = new Text3D(font3D, "Mia", new Point3f(-.1f, -.3f, -4.8f));
        lastName.setString("Mia");

        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f blueish = new Color3f(.35f, 0.1f, 0.6f);
        Appearance appearance = new Appearance();
        Material material = new Material(blueish, blueish, blueish, white, 82.0f);
        material.setLightingEnable(true);
        appearance.setMaterial(material);

        Shape3D shape3D1 = new Shape3D();
        shape3D1.setGeometry(firstName);
        shape3D1.setAppearance(appearance);

        Shape3D shape3D2 = new Shape3D();
        shape3D2.setGeometry(lastName);
        shape3D2.setAppearance(appearance);

        TransformGroup transformGroup = new TransformGroup();
        Transform3D transform3D = new Transform3D();
        Vector3f v3f = new Vector3f(-1.0f, -1.0f, -4f);
        transform3D.setTranslation(v3f);
        transformGroup.setTransform(transform3D);
        transformGroup.addChild(shape3D1);
        transformGroup.addChild(shape3D2);
        branchGroup.addChild(transformGroup);
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setTurn(boolean turn) {
        isTurn = turn;
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}