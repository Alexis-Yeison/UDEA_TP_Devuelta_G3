import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

// Metodo constructor
public class FrmDevuelta extends JFrame {

    private int MAXIMO_VALOR_MONEDA = 1000;

    private int[] denominaciones = new int[] {100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50};
    private int[] existencias = new int[denominaciones.length];
    private String[] encabezados = new String[]{"Cantidad", "Presentación", "Denominación"};
    
    private JComboBox cmbDenominacion;
    private JTable tblDevuelta;
    
    JTextField txtExistencia, txtDevuelta;
    

    public FrmDevuelta(){
        setSize(400,400);
        setTitle("Calculo devuelta");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblDenominacion = new JLabel("Denominacion");
        lblDenominacion.setBounds(10,10,100,25);
        getContentPane().add(lblDenominacion);

        cmbDenominacion = new JComboBox<>();
        for(int denominacion : denominaciones){
            cmbDenominacion.addItem(denominacion);
        }
        cmbDenominacion.setBounds(110,10,100,25);
        getContentPane().add(cmbDenominacion);

        JButton btnActulizarExistencia = new JButton("Actualizar");
        btnActulizarExistencia.setBounds(10,40,170,25);
        getContentPane().add(btnActulizarExistencia);

        txtExistencia = new JTextField();
        txtExistencia.setBounds(200,40,100,25);
        getContentPane().add(txtExistencia);

        JLabel lblDevuelta = new JLabel("Valor a devolver");
        lblDevuelta.setBounds(10,90,100,25);
        getContentPane().add(lblDevuelta);

        txtDevuelta = new JTextField();
        txtDevuelta.setBounds(120,90,100,25);
        getContentPane().add(txtDevuelta);

        JButton btnDevuelta = new JButton("Devolver");
        btnDevuelta.setBounds(240,90,100,25);
        getContentPane().add(btnDevuelta);

        tblDevuelta = new JTable();
        JScrollPane spDevuelta = new JScrollPane(tblDevuelta);
        spDevuelta.setBounds(10,150,370,180);
        getContentPane().add(spDevuelta);

        DefaultTableModel dtm = new DefaultTableModel(null, encabezados);
        tblDevuelta.setModel(dtm);

        // Agregar eventos
        btnActulizarExistencia.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                actualizarExistencia();
            }
        });

        cmbDenominacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                mostrarExistencia();
            }
        });
        
        btnDevuelta.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                calcularDevuelta();
            }
        });
        
    }

    private void actualizarExistencia(){
        // JOptionPane.showMessageDialog(null,"Hizo clic en el botón");
        if(cmbDenominacion.getSelectedIndex() >= 0){
            existencias[cmbDenominacion.getSelectedIndex()] = Integer.parseInt(txtExistencia.getText());
        }
    }

    private void mostrarExistencia(){
        // JOptionPane.showMessageDialog(null,"Seleccionó una opción de la lista");
        if(cmbDenominacion.getSelectedIndex() >= 0){
            txtExistencia.setText(String.valueOf(existencias[cmbDenominacion.getSelectedIndex()]));
        }
    }

    private void calcularDevuelta(){
        int[] cantidad = new int[denominaciones.length];
        int devuelta = Integer.parseInt(txtDevuelta.getText());
        int filaDenominacion = 0;

        while(devuelta > 0 && filaDenominacion < denominaciones.length){
            int cantidadNecesaria = (devuelta - devuelta % denominaciones[filaDenominacion])/denominaciones[filaDenominacion];
            cantidad[filaDenominacion] = existencias[filaDenominacion] >= cantidadNecesaria ? cantidadNecesaria : existencias[filaDenominacion]; 

            devuelta -= cantidad[filaDenominacion] * denominaciones[filaDenominacion];

            filaDenominacion++;
        }

        int filasNecesarias = 0;

        for(int cantidades : cantidad){
            if(cantidades > 0){
                filasNecesarias ++;
            }
        }

        String[][] datos = new String[filasNecesarias][encabezados.length];
        filasNecesarias = 0;

        for(int i=0; i < denominaciones.length; i++){
            if(cantidad[i] > 0){
                datos[filasNecesarias][0] = String.valueOf(cantidad[i]);
                datos[filasNecesarias][1] = denominaciones[i] >= MAXIMO_VALOR_MONEDA ? "Billete" : "Moneda";
                datos[filasNecesarias][2] = String.valueOf(denominaciones[i]);
                filasNecesarias ++;
            }
        }

        DefaultTableModel dtm = new DefaultTableModel(datos, encabezados);
        tblDevuelta.setModel(dtm);

        if(devuelta > 0){
            JOptionPane.showMessageDialog(null, "Quedó pendiente un valor de $" + devuelta);
        }
    }

}
