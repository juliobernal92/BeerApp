package Vistas;

import Dao.DetallesRecetaDao;
import Dao.InsumosDao;
import Dao.RecetasDao;
import Entidades.DetallesReceta;
import Entidades.Insumos;
import Entidades.Recetas;
import com.formdev.flatlaf.FlatDarkLaf;
import groovy.xml.Entity;
import java.awt.event.KeyEvent;
import static java.awt.image.ImageObserver.HEIGHT;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
public class Main extends javax.swing.JFrame {
    Insumos insumos = new Insumos();
    InsumosDao insumosDao = new InsumosDao();
    RecetasDao recetasDao = new RecetasDao();
    Recetas recetas = new Recetas();
    DetallesRecetaDao detallesDao = new DetallesRecetaDao();
    DetallesReceta detalles = new DetallesReceta();
    List<Insumos> listaInsumos = new ArrayList<>();
    List<Recetas> listaRecetas = new ArrayList<>();
    List<DetallesReceta>listaDetalles = new ArrayList<>();
    List<String> estilosReceta = recetasDao.obtenerEstilosReceta();
    List<String> tiposInsumo = insumosDao.obtenerTiposInsumo();
    private String estiloSeleccionado;
    private int totalValor = 0;
    
    public Main() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        actualizarTablaInsumos();
        actualizarTablaReceta();
        cargarListaInsumos();
        deshabilitarBotones();
        deshabilitarBotonesReceta();
        
        
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(estilosReceta.toArray(new String[0]));
        cbrecetas.setModel(comboBoxModel);

        for (String tipo : tiposInsumo) {
            cbtipoinsumo.addItem(tipo);
        }
    }

    private void habilitarBotones() {
        btneditar.setEnabled(true);
        btneliminar.setEnabled(true);
        btnañadir.setEnabled(false);

    }

    private void deshabilitarBotones() {
        btneditar.setEnabled(false);
        btneliminar.setEnabled(false);
        btnañadir.setEnabled(true);

    }

    private void limpiarCampos() {
        txtinsumo.setText("");
        txtcantidad.setText("");
        txtcostounitario.setText("");
        txtidinsumo.setText("");
    }
    
    private void habilitarBotonesReceta() {
        btneditarreceta.setEnabled(true);
        btneliminarreceta.setEnabled(true);
        btnañadirreceta.setEnabled(false);

    }
    
    private void habilitarBotonesRecetaInsumos(){
        btneditarinsumo.setEnabled(true);
        btneliminarinsumo.setEnabled(true);
        btnañadirinsumo.setEnabled(false);
    }
    
    private void deshabilitarBotonesRecetaInsumos(){
        btneditarinsumo.setEnabled(false);
        btneliminarinsumo.setEnabled(false);
        btnañadirinsumo.setEnabled(true);
    }

    private void deshabilitarBotonesReceta() {
        btneditarreceta.setEnabled(false);
        btneliminarreceta.setEnabled(false);
        btnañadirreceta.setEnabled(true);

    }
    
    private void limpiarCamposReceta() {
        txtestilo.setText("");
        txtlitros.setText("");
        txtidreceta.setText("");
    }
    
    private void limpiarCamposDetallesReceta() {
        txtiddetallesrecetas.setText("");
        txtcantidadinsumo.setText("");
    }
    
    
    
    private void actualizarTablaInsumos() {
        // Obtener la lista de producciones desde la base de datos
        InsumosDao insDao = new InsumosDao();
        List<Insumos> inslist = insDao.ObtenerTodos();

        // Limpiar los datos existentes en la tabla
        DefaultTableModel modelo = (DefaultTableModel) tbinsumos.getModel();
        modelo.setRowCount(0);

        // Agregar los nuevos datos a la tabla
        for (Insumos ins : inslist) {
            Object[] fila = {ins.getIdInsumos(), ins.getNombre(), ins.getTipoInsumo(), ins.getCantidad(), ins.getCosto()};
            modelo.addRow(fila);
        }
    }
    
    private void cargarListaInsumos() {
        try {
            listaInsumos.clear();
            listaInsumos = insumosDao.ObtenerTodos();
            DefaultTableModel model = (DefaultTableModel) tbinsumos.getModel();
            model.setRowCount(0);

            for (Insumos stk : listaInsumos) {
                Object[] row = new Object[5];
                row[0] = stk.getIdInsumos();
                row[1] = stk.getNombre();
                row[2] = stk.getTipoInsumo();
                row[3] = stk.getCantidad();
                row[4] = stk.getCosto();
                model.addRow(row);

            }

        } catch (Exception e) {
            System.err.println(e.getMessage());

        }

    }
    
    public void actualizarTablaReceta() {
        // Obtener la lista de producciones desde la base de datos
        RecetasDao recetasDao = new RecetasDao();
        List<Recetas> recetaList = recetasDao.ObtenerTodos();

        // Limpiar los datos existentes en la tabla
        DefaultTableModel modelo = (DefaultTableModel) tbrecetas.getModel();
        modelo.setRowCount(0);

        // Agregar los nuevos datos a la tabla
        for (Recetas receta : recetaList) {
            Object[] fila = {receta.getIdRecetas(), receta.getNombre(), receta.getLitros()};
            modelo.addRow(fila);
        }

        DefaultTableModel modelo2 = (DefaultTableModel) tbdetallesreceta.getModel();
        modelo2.setRowCount(0); // Limpiar los datos actuales de la tabla

        String idRecetaText = txtidreceta.getText().trim();

        if (!idRecetaText.isEmpty()) {
            int idReceta = Integer.parseInt(idRecetaText);

            List<DetallesReceta> recetaInsumos = detallesDao.obtenerRecetaInsumoPorEstiloIdReceta(estiloSeleccionado, idReceta);

            for (DetallesReceta recetaInsumo : recetaInsumos) {
                Insumos stockInsumo = insumosDao.obtenerStockInsumoPorId(recetaInsumo.getIdInsumos());

                modelo2.addRow(new Object[]{
                    recetaInsumo.getIdDetalles(),
                    stockInsumo.getTipoInsumo(),
                    stockInsumo.getNombre(),
                    recetaInsumo.getCantidad(),
                    recetaInsumo.getCostoUnitario(),
                    recetaInsumo.getIdInsumos()
                });
            }
        }
       
        estilosReceta = recetasDao.obtenerEstilosReceta(); 

        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>(estilosReceta.toArray(new String[0]));
        cbrecetas.setModel(comboBoxModel);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtinsumo = new javax.swing.JTextField();
        txtcantidad = new javax.swing.JTextField();
        txtcostounitario = new javax.swing.JTextField();
        btnañadir = new javax.swing.JButton();
        btneditar = new javax.swing.JButton();
        btneliminar = new javax.swing.JButton();
        btnlimpiar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbinsumos = new javax.swing.JTable();
        txtidinsumo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbtipoinsumoInsumos = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtidreceta = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtestilo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtlitros = new javax.swing.JTextField();
        btnañadirreceta = new javax.swing.JButton();
        btneditarreceta = new javax.swing.JButton();
        btneliminarreceta = new javax.swing.JButton();
        btnlimpiarreceta = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbrecetas = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbdetallesreceta = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtiddetallesrecetas = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cbrecetas = new javax.swing.JComboBox<>();
        btnseleccionarreceta = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        cbtipoinsumo = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        cbnombreinsumo = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        txtcantidadinsumo = new javax.swing.JTextField();
        btnañadirinsumo = new javax.swing.JButton();
        btneditarinsumo = new javax.swing.JButton();
        btneliminarinsumo = new javax.swing.JButton();
        btnlimpiarinsumo = new javax.swing.JButton();
        lblcosto = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getSize()+4f));
        jLabel1.setText("Insumo");

        jLabel2.setFont(jLabel2.getFont().deriveFont(jLabel2.getFont().getSize()+4f));
        jLabel2.setText("Cantidad");

        jLabel3.setFont(jLabel3.getFont().deriveFont(jLabel3.getFont().getSize()+4f));
        jLabel3.setText("Costo Unitario");

        txtinsumo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        txtcantidad.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        txtcostounitario.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        btnañadir.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnañadir.setText("Añadir");
        btnañadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnañadirActionPerformed(evt);
            }
        });

        btneditar.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btneditar.setText("Editar");
        btneditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditarActionPerformed(evt);
            }
        });

        btneliminar.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btneliminar.setText("Eliminar");
        btneliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarActionPerformed(evt);
            }
        });

        btnlimpiar.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnlimpiar.setText("Limpiar");
        btnlimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlimpiarActionPerformed(evt);
            }
        });

        tbinsumos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Insumo", "Tipo Insumo", "Cantidad", "Costo Unitario"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbinsumos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbinsumosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbinsumos);
        if (tbinsumos.getColumnModel().getColumnCount() > 0) {
            tbinsumos.getColumnModel().getColumn(0).setResizable(false);
            tbinsumos.getColumnModel().getColumn(0).setPreferredWidth(20);
            tbinsumos.getColumnModel().getColumn(1).setResizable(false);
            tbinsumos.getColumnModel().getColumn(1).setPreferredWidth(150);
            tbinsumos.getColumnModel().getColumn(2).setResizable(false);
            tbinsumos.getColumnModel().getColumn(3).setResizable(false);
            tbinsumos.getColumnModel().getColumn(3).setPreferredWidth(50);
            tbinsumos.getColumnModel().getColumn(4).setResizable(false);
        }

        txtidinsumo.setEditable(false);
        txtidinsumo.setBackground(new java.awt.Color(102, 102, 102));
        txtidinsumo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        txtidinsumo.setFocusable(false);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel5.setText("Tipo Insumo");

        cbtipoinsumoInsumos.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cbtipoinsumoInsumos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Malta", "Lupulo", "Levadura", "Otros" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtidinsumo, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtinsumo, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbtipoinsumoInsumos, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtcantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtcostounitario, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnañadir, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btneditar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btneliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnlimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtinsumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtcantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtcostounitario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnañadir)
                    .addComponent(btneditar)
                    .addComponent(btneliminar)
                    .addComponent(btnlimpiar)
                    .addComponent(txtidinsumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cbtipoinsumoInsumos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("INSUMOS", jPanel1);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Recetas");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Añadir Receta");

        txtidreceta.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel7.setText("Estilo");

        txtestilo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel8.setText("Litros");

        txtlitros.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        btnañadirreceta.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnañadirreceta.setText("Añadir");
        btnañadirreceta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnañadirrecetaActionPerformed(evt);
            }
        });

        btneditarreceta.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btneditarreceta.setText("Editar");

        btneliminarreceta.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btneliminarreceta.setText("Eliminar");

        btnlimpiarreceta.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnlimpiarreceta.setText("Limpiar");

        tbrecetas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Estilo", "Litros"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tbrecetas);
        if (tbrecetas.getColumnModel().getColumnCount() > 0) {
            tbrecetas.getColumnModel().getColumn(0).setResizable(false);
            tbrecetas.getColumnModel().getColumn(0).setPreferredWidth(20);
            tbrecetas.getColumnModel().getColumn(1).setResizable(false);
            tbrecetas.getColumnModel().getColumn(2).setResizable(false);
            tbrecetas.getColumnModel().getColumn(2).setPreferredWidth(20);
        }

        tbdetallesreceta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Tipo Insumo", "Nombre", "Cantidad", "Costo", "ID Insumo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbdetallesreceta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbdetallesrecetaMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbdetallesreceta);
        if (tbdetallesreceta.getColumnModel().getColumnCount() > 0) {
            tbdetallesreceta.getColumnModel().getColumn(0).setResizable(false);
            tbdetallesreceta.getColumnModel().getColumn(0).setPreferredWidth(10);
            tbdetallesreceta.getColumnModel().getColumn(1).setResizable(false);
            tbdetallesreceta.getColumnModel().getColumn(2).setResizable(false);
            tbdetallesreceta.getColumnModel().getColumn(3).setResizable(false);
            tbdetallesreceta.getColumnModel().getColumn(4).setResizable(false);
            tbdetallesreceta.getColumnModel().getColumn(5).setResizable(false);
            tbdetallesreceta.getColumnModel().getColumn(5).setPreferredWidth(10);
        }

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Seleccionar Receta");

        txtiddetallesrecetas.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel10.setText("Receta");

        cbrecetas.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cbrecetas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnseleccionarreceta.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnseleccionarreceta.setText("Seleccionar");
        btnseleccionarreceta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnseleccionarrecetaActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel11.setText("Añadir Insumos: ");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel12.setText("Tipo");

        cbtipoinsumo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cbtipoinsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbtipoinsumoActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel13.setText("Nombre");

        cbnombreinsumo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cbnombreinsumo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel14.setText("Cantidad");

        txtcantidadinsumo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

        btnañadirinsumo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnañadirinsumo.setText("Añadir");
        btnañadirinsumo.setMaximumSize(new java.awt.Dimension(83, 29));
        btnañadirinsumo.setMinimumSize(new java.awt.Dimension(83, 29));
        btnañadirinsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnañadirinsumoActionPerformed(evt);
            }
        });

        btneditarinsumo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btneditarinsumo.setText("Editar");
        btneditarinsumo.setMaximumSize(new java.awt.Dimension(83, 29));
        btneditarinsumo.setMinimumSize(new java.awt.Dimension(83, 29));
        btneditarinsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditarinsumoActionPerformed(evt);
            }
        });

        btneliminarinsumo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btneliminarinsumo.setText("Eliminar");
        btneliminarinsumo.setMaximumSize(new java.awt.Dimension(83, 29));
        btneliminarinsumo.setMinimumSize(new java.awt.Dimension(83, 29));
        btneliminarinsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneliminarinsumoActionPerformed(evt);
            }
        });

        btnlimpiarinsumo.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        btnlimpiarinsumo.setText("Limpiar");
        btnlimpiarinsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlimpiarinsumoActionPerformed(evt);
            }
        });

        lblcosto.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblcosto.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblcosto.setText("Costo Total de la Receta");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtidreceta, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel7))
                            .addComponent(btnañadirreceta, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(txtestilo, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtlitros))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(btneditarreceta, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btneliminarreceta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnlimpiarreceta, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnañadirinsumo, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btneditarinsumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btneliminarinsumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnlimpiarinsumo)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblcosto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane3)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cbtipoinsumo, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cbnombreinsumo, 0, 128, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtcantidadinsumo, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(17, 17, 17))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtiddetallesrecetas, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbrecetas, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnseleccionarreceta)
                        .addGap(226, 226, 226))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtiddetallesrecetas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(cbrecetas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnseleccionarreceta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtidreceta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtestilo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtlitros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(cbtipoinsumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(cbnombreinsumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(txtcantidadinsumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnañadirreceta)
                    .addComponent(btneditarreceta)
                    .addComponent(btneliminarreceta)
                    .addComponent(btnlimpiarreceta)
                    .addComponent(btnañadirinsumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btneditarinsumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btneliminarinsumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnlimpiarinsumo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(lblcosto, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("RECETAS", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1193, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 632, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("STOCK", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbinsumosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbinsumosMouseClicked
        // TODO add your handling code here:
        int i =tbinsumos.getSelectedRow();
        TableModel model = tbinsumos.getModel();
        txtidinsumo.setText(model.getValueAt(i, 0).toString());
        txtinsumo.setText(model.getValueAt(i, 1).toString());
        cbtipoinsumoInsumos.setSelectedItem(model.getValueAt(i, 2).toString());
        txtcantidad.setText(model.getValueAt(i, 3).toString());
        habilitarBotones();
    }//GEN-LAST:event_tbinsumosMouseClicked

    private void btnlimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlimpiarActionPerformed
        // TODO add your handling code here:
        limpiarCampos();
    }//GEN-LAST:event_btnlimpiarActionPerformed

    private void btneliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarActionPerformed
        // TODO add your handling code here:
        int numerofila= tbinsumos.getSelectedRow();
        if(numerofila>=0){
            int option = JOptionPane.showConfirmDialog(null, "¿Estas seguro de que quieres eliminar?", "BeerApp", JOptionPane.YES_NO_OPTION);
            if(option==0){
                TableModel  model = tbinsumos.getModel();
                String id = model.getValueAt(numerofila, 0).toString();
                if (insumosDao.Eliminar(Integer.parseInt(id))){
                    JOptionPane.showMessageDialog(null, "Insumo Eliminado", "BeerApp", HEIGHT);
                    cargarListaInsumos();
                    limpiarCampos();
                }else{
                    JOptionPane.showMessageDialog(null, "No se puede Eliminar", "BeerApp", HEIGHT);

                }
            }else{
                JOptionPane.showMessageDialog(null, "Por favor seleccione una fila a eliminar", "BeerApp", HEIGHT);

            }
        }
        evt.setSource((char) KeyEvent.VK_CLEAR);
        deshabilitarBotones();
        txtinsumo.requestFocus();
    }//GEN-LAST:event_btneliminarActionPerformed

    private void btneditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditarActionPerformed
        // TODO add your handling code here:
        String insumo = txtinsumo.getText().trim();
        String cantidad = txtcantidad.getText().trim();
        String tipoinsumo = cbtipoinsumoInsumos.getSelectedItem().toString().trim();
        String costounit = txtcostounitario.getText().trim();
        String id = txtidinsumo.getText().trim();

        if (!insumo.isEmpty() && !tipoinsumo.isEmpty() && !cantidad.isEmpty() && !costounit.isEmpty() && !id.isEmpty()) {
            try {
                Insumos ins = new Insumos();
                ins.setNombre(insumo);
                ins.setTipoInsumo(tipoinsumo);
                ins.setCantidad(new BigDecimal(cantidad));
                ins.setCosto(Integer.parseInt(costounit));
                ins.setIdInsumos(Integer.parseInt(id));

                if ( insumosDao.Actualizar(ins)) {
                    JOptionPane.showMessageDialog(null, "Insumo Actualizado","BeerApp", HEIGHT);
                    actualizarTablaInsumos();
                    limpiarCampos();
                    txtinsumo.requestFocus();
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }else{
            JOptionPane.showMessageDialog(null, "Por favor complete todos los campos","BeerApp", HEIGHT);

        }
        deshabilitarBotones();
    }//GEN-LAST:event_btneditarActionPerformed

    private void btnañadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnañadirActionPerformed
        // TODO add your handling code here:
        
        String nombre = txtinsumo.getText().trim();
        String tipoinsumo = cbtipoinsumoInsumos.getSelectedItem().toString().trim();
        String cantidad = txtcantidad.getText().trim();
        String costo = txtcostounitario.getText().trim();
        
        if (!nombre.isEmpty()  &&  !tipoinsumo.isEmpty() && !cantidad.isEmpty() && !costo.isEmpty()) {
            try {
                Insumos stockinsumos = new Insumos();
                stockinsumos.setNombre(nombre);
                stockinsumos.setTipoInsumo(tipoinsumo);
                stockinsumos.setCantidad(new BigDecimal(cantidad));
                stockinsumos.setCosto(Integer.parseInt(costo));
                

                if (insumosDao.Insertar(stockinsumos)) {
                    JOptionPane.showMessageDialog(null,"Insumo guardado");
                    actualizarTablaInsumos();
                    limpiarCampos();
                    txtinsumo.requestFocus();
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }else{
            JOptionPane.showMessageDialog(null, "Por favor complete todos los campos", "BeerApp", HEIGHT);
            
        }
        evt.setSource((char) KeyEvent.VK_CLEAR);
        txtinsumo.requestFocus();
        deshabilitarBotones();
    }//GEN-LAST:event_btnañadirActionPerformed

    private void btnañadirrecetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnañadirrecetaActionPerformed
        // TODO add your handling code here:
        String estilo = txtestilo.getText().trim();
        String litros = txtlitros.getText().trim();

        if (!estilo.isEmpty() && !litros.isEmpty()) {
            try {
                if (recetasDao.existeRecetaPorEstilo(estilo)) {
                    JOptionPane.showMessageDialog(null, "El estilo de receta ya existe", "Error", JOptionPane.ERROR_MESSAGE);

                    limpiarCamposReceta();
                    txtestilo.requestFocus();
                    return;
                }

                Recetas receta = new Recetas();
                receta.setNombre(estilo);
                receta.setLitros(new BigDecimal(litros));

                if (recetasDao.Insertar(receta)) {
                    JOptionPane.showMessageDialog(null, "Receta Añadida Exitosamente");
                    actualizarTablaReceta();
                    limpiarCamposReceta();
                    txtestilo.requestFocus();
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor complete todos los campos", "CraftBeer Manager", JOptionPane.WARNING_MESSAGE);
        }
        evt.setSource((char) KeyEvent.VK_CLEAR);
        txtestilo.requestFocus();
        deshabilitarBotonesReceta();
    }//GEN-LAST:event_btnañadirrecetaActionPerformed

    private void cbtipoinsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbtipoinsumoActionPerformed
        // TODO add your handling code here:
        String tipoSeleccionado = (String) cbtipoinsumo.getSelectedItem();

        // Obtener los nombres de insumo correspondientes al tipo seleccionado
        List<String> nombresInsumo = insumosDao.obtenerNombresInsumoPorTipo(tipoSeleccionado);

        // Limpiar el JComboBox "cbnombre"
        cbnombreinsumo.removeAllItems();

        // Agregar los nombres de insumo al JComboBox "cbnombre"
        for (String nombre : nombresInsumo) {
            cbnombreinsumo.addItem(nombre);
        }
    }//GEN-LAST:event_cbtipoinsumoActionPerformed

    private void btnseleccionarrecetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnseleccionarrecetaActionPerformed
        // TODO add your handling code here:
        estiloSeleccionado = (String) cbrecetas.getSelectedItem();
        //String estiloSeleccionado = (String) cbreceta.getSelectedItem();

        // Obtener los datos de la receta seleccionada desde la base de datos
        Recetas receta = recetasDao.obtenerRecetaPorEstilo(estiloSeleccionado);

        if (receta != null) {
            // Asignar los valores de la receta a los campos de texto
            txtidreceta.setText(String.valueOf(receta.getIdRecetas()));
            txtestilo.setText(receta.getNombre());
            txtlitros.setText(receta.getLitros().toString());

            DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("Id");
            modelo.addColumn("Tipo Insumo");
            modelo.addColumn("Nombre");
            modelo.addColumn("Cantidad");
            modelo.addColumn("Costo");
            modelo.addColumn("Id Insumo");

            int idReceta = Integer.parseInt(txtidreceta.getText().trim());

            List<DetallesReceta> recetaInsumos = detallesDao.obtenerRecetaInsumoPorEstiloIdReceta(estiloSeleccionado, idReceta);

            for (DetallesReceta recetaInsumo : recetaInsumos) {
                Insumos stockInsumo = insumosDao.obtenerStockInsumoPorId(recetaInsumo.getIdInsumos());

                modelo.addRow(new Object[]{
                    recetaInsumo.getIdDetalles(),
                    stockInsumo.getTipoInsumo(),
                    stockInsumo.getNombre(),
                    recetaInsumo.getCantidad(),
                    recetaInsumo.getCostoUnitario(),
                    recetaInsumo.getIdInsumos()
                });
            }
            tbdetallesreceta.setModel(modelo);
        }

        DefaultTableModel model = (DefaultTableModel) tbdetallesreceta.getModel();
        int rowCount = model.getRowCount();
        totalValor = 0;

        for (int i = 0; i < rowCount; i++) {
            int precioUnitario = Integer.parseInt(model.getValueAt(i, 4).toString());

            totalValor += precioUnitario;
        }

        float costolitro = totalValor / 40;
        lblcosto.setText("Costo de la Receta: " + totalValor + " Costo por Litro: " + costolitro);

    }//GEN-LAST:event_btnseleccionarrecetaActionPerformed

    private void btnañadirinsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnañadirinsumoActionPerformed
        // TODO add your handling code here:
        String tipoInsumo = cbtipoinsumo.getSelectedItem().toString();
        String nombreInsumo = cbnombreinsumo.getSelectedItem().toString();
        String cantidad = txtcantidadinsumo.getText().trim();
        BigDecimal cantBD = new BigDecimal(cantidad);
        int idReceta = Integer.parseInt(txtidreceta.getText().trim());

        if (!tipoInsumo.isEmpty() && !nombreInsumo.isEmpty() && !cantidad.isEmpty()) {
            try {
                int idInsumo = insumosDao.obtenerIdPorNombre(nombreInsumo);

                if (idInsumo != 0) {
                    DetallesReceta recetaInsumo = new DetallesReceta();
                    int costounit = insumosDao.obtenerCostoPorNombre(nombreInsumo);
                    int costo = cantBD.multiply(BigDecimal.valueOf(costounit)).intValue();
                    recetaInsumo.setCostoUnitario(costo);
                    recetaInsumo.setIdReceta(idReceta);
                    recetaInsumo.setIdInsumos(idInsumo);
                    recetaInsumo.setCantidad(cantBD);
                    

                    if (detallesDao.Insertar(recetaInsumo)) {
                        JOptionPane.showMessageDialog(null, "Insumo añadido a la receta exitosamente");
                        //limpiarCamposInsumo();
                        // Actualizar la tabla tbreceta con los nuevos datos
                        actualizarTablaReceta();
                        sumaTabla();
                      
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "El insumo seleccionado no existe", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor complete todos los campos", "CraftBeer Manager", JOptionPane.WARNING_MESSAGE);
        }
        txtcantidad.setText("");
        txtcantidad.requestFocus();
        limpiarCamposDetallesReceta();
        deshabilitarBotonesRecetaInsumos();
    }//GEN-LAST:event_btnañadirinsumoActionPerformed

//    private void cargarListaReceta() {
//        try {
//            listaRecetas.clear();
//            listaRecetas = recetasDao.ObtenerTodos();
//            DefaultTableModel model = (DefaultTableModel) tbestilos.getModel();
//            model.setRowCount(0);
//
//            for (Receta rct : listaReceta) {
//                Object[] row = new Object[3];
//                row[0] = rct.getIdReceta();
//                row[1] = rct.getEstilo();
//                row[2] = rct.getLitros();
//                model.addRow(row);
//
//            }
//
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//
//        }
//
//    }
//    
    
    private void btneliminarinsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneliminarinsumoActionPerformed
        // TODO add your handling code here:
        int numerofila = tbdetallesreceta.getSelectedRow();
        if (numerofila >= 0) {
            int option = JOptionPane.showConfirmDialog(null, "¿Estas seguro de que quieres eliminar?", "Confirmar Accion", JOptionPane.YES_NO_OPTION);
            if (option == 0) {
                TableModel model = tbdetallesreceta.getModel();
                String iddetalle = model.getValueAt(numerofila, 0).toString();
                int iddetalleint = Integer.parseInt(iddetalle);
                if (detallesDao.eliminar(iddetalleint)) {
                    JOptionPane.showMessageDialog(null, "Producto Eliminado", "CraftBeer Manager", HEIGHT);
                    //
                    actualizarTablaReceta();
                    limpiarCamposReceta();
                } else {
                    JOptionPane.showMessageDialog(null, "No se puede Eliminar", "CraftBeer Manager", HEIGHT);

                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor seleccione una fila a eliminar", "CraftBeer Manager", HEIGHT);

            }
        }
        evt.setSource((char) KeyEvent.VK_CLEAR);
        cbtipoinsumo.requestFocus();
        deshabilitarBotonesRecetaInsumos();
        limpiarCamposDetallesReceta();
    }//GEN-LAST:event_btneliminarinsumoActionPerformed

    private void tbdetallesrecetaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbdetallesrecetaMouseClicked
        // TODO add your handling code here:
        int i = tbdetallesreceta.getSelectedRow();
        TableModel model = tbdetallesreceta.getModel();
        txtiddetallesrecetas.setText(model.getValueAt(i, 0).toString());
        cbtipoinsumo.setSelectedItem(model.getValueAt(i, 1));
        cbnombreinsumo.setSelectedItem(model.getValueAt(i, 2));
        txtcantidadinsumo.setText(model.getValueAt(i, 3).toString());
        txtidinsumo.setText(model.getValueAt(i, 5).toString());
        
        habilitarBotonesRecetaInsumos();
    }//GEN-LAST:event_tbdetallesrecetaMouseClicked

    private void btneditarinsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditarinsumoActionPerformed
        // TODO add your handling code here:
        String tipo = cbtipoinsumo.getSelectedItem().toString().trim();
        String nombre = cbnombreinsumo.getSelectedItem().toString().trim();
        String cantidad = txtcantidadinsumo.getText().trim();
        String id = txtiddetallesrecetas.getText().trim();
        String idreceta = txtidreceta.getText().trim();
        String idinsumo  = txtidinsumo.getText().trim();

        if (!tipo.isEmpty() && !nombre.isEmpty() && !cantidad.isEmpty() && !id.isEmpty()) {
            try {
                DetallesReceta recIns = new DetallesReceta();
                recIns.setCantidad(new BigDecimal(cantidad));
                recIns.setIdDetalles(Integer.parseInt(id));
                recIns.setIdReceta(Integer.parseInt(idreceta));
                recIns.setIdInsumos(Integer.parseInt(idinsumo));
                int costounit = insumosDao.obtenerCostoPorNombre(nombre);
                int costo = costounit * Integer.parseInt(cantidad);
                JOptionPane.showMessageDialog(rootPane, "Costo del Insumo: "+costounit+ "  Costo nuevo: "+costo+"Id Insumo: "+idinsumo);
                recIns.setCostoUnitario(costo);

                if (detallesDao.Actualizar(recIns)) {
                    JOptionPane.showMessageDialog(null, "Insumo Actualizado", "CraftBeer Manager", HEIGHT);
                    actualizarTablaReceta();
                    limpiarCamposReceta();
                    cbtipoinsumo.requestFocus();
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor complete todos los campos", "CraftBeer Manager", HEIGHT);

        }
        deshabilitarBotonesRecetaInsumos();
        limpiarCamposDetallesReceta();
        
    }//GEN-LAST:event_btneditarinsumoActionPerformed

    private void btnlimpiarinsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlimpiarinsumoActionPerformed
        // TODO add your handling code here:
        limpiarCamposDetallesReceta();
    }//GEN-LAST:event_btnlimpiarinsumoActionPerformed

    
    private void sumaTabla() {
        DefaultTableModel model = (DefaultTableModel) tbdetallesreceta.getModel();
        int rowCount = model.getRowCount();
        totalValor = 0;

        for (int i = 0; i < rowCount; i++) {
            int precioUnitario = Integer.parseInt(model.getValueAt(i, 4).toString());

            totalValor += precioUnitario;
        }

        float costolitro = totalValor/40;
        lblcosto.setText("Costo de la Receta: "+totalValor+" Costo por Litro: "+costolitro);
        
        DetallesReceta details = new DetallesReceta();
        String iddetallesstr = txtiddetallesrecetas.getText().trim();
        if(detallesDao.actualizarCostoUnitario(Integer.parseInt(iddetallesstr), totalValor)){
            cbtipoinsumo.requestFocus();
        }
        
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            // Aplicar el tema FlatLaf Dark
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            // Si no se puede aplicar el tema, muestra un mensaje de error o realiza alguna acción adecuada.
            ex.printStackTrace();
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnañadir;
    private javax.swing.JButton btnañadirinsumo;
    private javax.swing.JButton btnañadirreceta;
    private javax.swing.JButton btneditar;
    private javax.swing.JButton btneditarinsumo;
    private javax.swing.JButton btneditarreceta;
    private javax.swing.JButton btneliminar;
    private javax.swing.JButton btneliminarinsumo;
    private javax.swing.JButton btneliminarreceta;
    private javax.swing.JButton btnlimpiar;
    private javax.swing.JButton btnlimpiarinsumo;
    private javax.swing.JButton btnlimpiarreceta;
    private javax.swing.JButton btnseleccionarreceta;
    private javax.swing.JComboBox<String> cbnombreinsumo;
    private javax.swing.JComboBox<String> cbrecetas;
    private javax.swing.JComboBox<String> cbtipoinsumo;
    private javax.swing.JComboBox<String> cbtipoinsumoInsumos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblcosto;
    private javax.swing.JTable tbdetallesreceta;
    private javax.swing.JTable tbinsumos;
    private javax.swing.JTable tbrecetas;
    private javax.swing.JTextField txtcantidad;
    private javax.swing.JTextField txtcantidadinsumo;
    private javax.swing.JTextField txtcostounitario;
    private javax.swing.JTextField txtestilo;
    private javax.swing.JTextField txtiddetallesrecetas;
    private javax.swing.JTextField txtidinsumo;
    private javax.swing.JTextField txtidreceta;
    private javax.swing.JTextField txtinsumo;
    private javax.swing.JTextField txtlitros;
    // End of variables declaration//GEN-END:variables
}
