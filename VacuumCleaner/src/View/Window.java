package View;

import Control.ControlView;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Tri Bằng - VUWIT14
 */
public class Window extends javax.swing.JFrame implements ChangeListener{
    private Dimension dimension;
    private Toolkit toolkit;
    private int widthScreen, heightScreen, width, height;
    public RobotStand robotStand;
    private ControlView controlView;
    private Evm evm;
    public final String[] listWidth = {"3", "4",  "5", "10"};
    public Window() {
        initComponents();
        //---------------
        evm = new Evm(this);
        jsSpeed.setMaximum(10);
        jsSpeed.setValue(5);
        jsSpeed.addChangeListener(this);
        controlView = new ControlView(this);
        jpEVm.setLayout(new BorderLayout());
        jpEVm.add(evm, "Center");
        jpAddRobot.setLayout(new BorderLayout());
        robotStand = new RobotStand(jpAddRobot.getWidth(), jpAddRobot.getHeight());
        jpAddRobot.add(robotStand, "Center");
        //---------------
        jcChooseEvmWidth.addActionListener(controlView);
        jbStart.addActionListener(controlView);
        //---------------
        toolkit = Toolkit.getDefaultToolkit();
        dimension = toolkit.getScreenSize();
        widthScreen = dimension.width;
        heightScreen = dimension.height;
        width = getWidth();
        height = getHeight();
        setMinimumSize(new Dimension(width, height));
        setLocation((widthScreen - width) / 2, (heightScreen - height) / 2);
        setDefaultCloseOperation(3);
        setTitle("Vacuum Cleaner");




        jcChooseEvmWidth.addKeyListener(evm);
    }

    public void setEvmSize(int width, int height){
        jlEvmSize.setText(width + " x " + height + " = " + (width * height));
        evm.createMatrix(width, height);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpEVm = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jcChooseEvmWidth = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jlEvmSize = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jpAddRobot = new javax.swing.JPanel();
        jbStart = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jsSpeed = new javax.swing.JSlider();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jlCountIphone = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jpEVm.setBackground(new java.awt.Color(153, 153, 255));
        jpEVm.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Bản đồ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 14))); // NOI18N

        javax.swing.GroupLayout jpEVmLayout = new javax.swing.GroupLayout(jpEVm);
        jpEVm.setLayout(jpEVmLayout);
        jpEVmLayout.setHorizontalGroup(
            jpEVmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 537, Short.MAX_VALUE)
        );
        jpEVmLayout.setVerticalGroup(
            jpEVmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 492, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Khởi tạo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 0, 14))); // NOI18N

        jcChooseEvmWidth.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jcChooseEvmWidth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3", "4", "5", "10" }));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel1.setText("Chiều dài:");

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel2.setText("Kích thước môi trường:");

        jlEvmSize.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jlEvmSize.setForeground(new java.awt.Color(102, 102, 102));
        jlEvmSize.setText("5 x 5 = 25");

        jLabel4.setText("      Click chọn robot và");

        jpAddRobot.setBackground(new java.awt.Color(204, 204, 255));
        jpAddRobot.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpAddRobot.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jpAddRobot.setLayout(new java.awt.BorderLayout());

        jbStart.setText("Bắt đầu");
        jbStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbStartActionPerformed(evt);
            }
        });

        jLabel3.setText("chọn nơi cần đặt trên bản đồ");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel5.setText("            Tốc độ robot:");

        jLabel6.setText("Số rác còn lại:");

        jlCountIphone.setText("00");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpAddRobot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcChooseEvmWidth, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlEvmSize, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jsSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jlCountIphone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcChooseEvmWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlEvmSize)
                .addGap(31, 31, 31)
                .addComponent(jpAddRobot, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(19, 19, 19)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jsSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jlCountIphone)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jbStart))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jpEVm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpEVm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbStartActionPerformed
        
    }//GEN-LAST:event_jbStartActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JButton jbStart;
    public javax.swing.JComboBox jcChooseEvmWidth;
    public javax.swing.JLabel jlCountIphone;
    private javax.swing.JLabel jlEvmSize;
    private javax.swing.JPanel jpAddRobot;
    private javax.swing.JPanel jpEVm;
    private javax.swing.JSlider jsSpeed;
    // End of variables declaration//GEN-END:variables

    public JComboBox getJcChooseEvmWidth() {
        return jcChooseEvmWidth;
    }

    public JButton getJbStart() {
        return jbStart;
    }

    public void stateChanged(ChangeEvent e) {
        Evm.setSpeed(11 - jsSpeed.getValue());
    }

    

    
}
