package GUIs;

import DAOs.DAOVenda;
import DAOs.DAOVendaHasProduto;
import Entidades.*;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerDateModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import tools.*;

public class GUIVenda extends JFrame {
    
    private final JPanel pnCentroNorte = new JPanel();
    private final JButton btnAdd = new JButton("Adicionar");
    private final JButton btnRem = new JButton("Remover");
    private final JButton btnCarregar = new JButton("Carregar dados");

    private JTable table = new JTable();
    private VendaHasProdutoTableModel tableModel;

    private DAOVendaHasProduto daoItensPedido = new DAOVendaHasProduto();


    ImageIcon iconeCreate = new ImageIcon(getClass().getResource("/icones/create.png"));
    ImageIcon iconeRetrieve = new ImageIcon(getClass().getResource("/icones/retrieve.png"));
    ImageIcon iconeUpdate = new ImageIcon(getClass().getResource("/icones/update.png"));
    ImageIcon iconeDelete = new ImageIcon(getClass().getResource("/icones/delete.png"));
    ImageIcon iconeSave = new ImageIcon(getClass().getResource("/icones/save.png"));
    ImageIcon iconeCancel = new ImageIcon(getClass().getResource("/icones/cancel.png"));
    ImageIcon iconeListar = new ImageIcon(getClass().getResource("/icones/list.png"));
    JButton btnCreate = new JButton(iconeCreate);
    JButton btnRetrieve = new JButton(iconeRetrieve);
    JButton btnUpdate = new JButton(iconeUpdate);
    JButton btnDelete = new JButton(iconeDelete);
    JButton btnSave = new JButton(iconeSave);
    JButton btnCancel = new JButton(iconeCancel);
    JButton btnList = new JButton(iconeListar);
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private Date data1;
    private JPanel pnNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private JPanel pnCentroMain = new JPanel(new GridLayout(1, 2));
    private JPanel pnCentro = new JPanel(new GridLayout(1, 2));
    private JPanel pnSul = new JPanel(new GridLayout(1, 1));
    private JPanel pnLeste = new JPanel(new GridLayout(1, 1));
    private JLabel lbIdVenda = new JLabel("IdVenda");
    private JTextField tfIdVenda = new JTextField(10);
    private JLabel lbDataVenda = new JLabel("DataVenda");
    private JTextField tfDataVenda = new JTextField(10);
    ScrollPane scroll = new ScrollPane();
    JTextArea jTextArea = new JTextArea();
    JPanel aviso = new JPanel();
    JLabel labelAviso = new JLabel("");
    String qualAcao = "";//variavel para facilitar insert e update
    DAOVenda daoVenda = new DAOVenda();
    Venda venda;
    private CaixaDeFerramentas ferramentas = new CaixaDeFerramentas();

    public GUIVenda() {
        setSize(1000, 360);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("CRUD - Venda");
        Container cp = getContentPane();
        cp = getContentPane();
        btnCreate.setToolTipText("Inserir novo registro");
        btnRetrieve.setToolTipText("Pesquisar por chave");
        btnUpdate.setToolTipText("Alterar");
        btnDelete.setToolTipText("Excluir");
        btnList.setToolTipText("Listar todos");
        btnSave.setToolTipText("Salvar");
        btnCancel.setToolTipText("Cancelar");
        cp.setLayout(new BorderLayout());
        cp.add(pnNorte, BorderLayout.NORTH);
        cp.add(pnCentroMain, BorderLayout.CENTER);
        cp.add(pnSul, BorderLayout.SOUTH);
        cp.add(pnLeste, BorderLayout.EAST);
        pnCentroMain.add(pnCentro);
        pnNorte.add(lbIdVenda);
        pnNorte.add(tfIdVenda);
        pnNorte.add(btnRetrieve);
        pnNorte.add(btnCreate);
        pnNorte.add(btnUpdate);
        pnNorte.add(btnDelete);
        pnNorte.add(btnSave);
        pnNorte.add(btnList);
        btnCancel.setVisible(false);
        btnDelete.setVisible(false);
        btnCreate.setVisible(false);
        btnSave.setVisible(false);
        btnUpdate.setVisible(false);
        pnCentro.add(lbDataVenda);
        pnCentro.add(tfDataVenda);
        pnSul.setBackground(Color.red);
        scroll.add(jTextArea);
        pnSul.add(scroll);
        tfDataVenda.setEditable(false);
        //////////////////////
        
        //////////////////////
        btnRetrieve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<VendaHasProduto> lc = daoItensPedido.list();
                    tableModel.setDados(lc);
                    tableModel.fireTableDataChanged();
                    //btnAdd.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao carregar os dados..." + ex.getMessage());
                }
                try {
                    tfIdVenda.setBackground(Color.white);
                    jTextArea.setText("");
                    venda = new Venda();
                    int identificador = Integer.valueOf(tfIdVenda.getText());
                    venda.setIdVenda(identificador);
                    venda = daoVenda.obter(venda.getIdVenda());
                    if (venda == null) {
                        pnNorte.setBackground(Color.red);
                        tfDataVenda.setText("");
                        btnCreate.setVisible(true);
                    } else {
                        pnNorte.setBackground(Color.green);
                        tfDataVenda.setText(sdf.format(venda.getDataVenda()));
                        btnUpdate.setVisible(true);
                        btnDelete.setVisible(true);
                        btnCreate.setVisible(false);
                    }
                    tfIdVenda.setEditable(false);
                    tfDataVenda.setEditable(false);
                    tfIdVenda.selectAll();
                } catch (Exception erro) {
                    pnNorte.setBackground(Color.yellow);
                    tfIdVenda.requestFocus();
                    tfIdVenda.setBackground(Color.red);
                    jTextArea.setText("Erro... \n");
                    jTextArea.append(erro.getMessage());
                }
                tfIdVenda.setEditable(true);
                
            }
        });
        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tfIdVenda.setEditable(false);
                tfDataVenda.requestFocus();
                btnCreate.setVisible(false);
                btnSave.setVisible(true);
                qualAcao = "incluir";
                venda = new Venda();
                tfIdVenda.setEditable(false);
            }
        });
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    jTextArea.setText("");
                    venda = new Venda();
                    venda.setIdVenda(Integer.valueOf(tfIdVenda.getText()));
                    sdf.setLenient(false);
                    data1 = sdf.parse(tfDataVenda.getText());
                    try {
                        venda.setDataVenda(sdf.parse(tfDataVenda.getText()));
                    } catch (ParseException ex) {
                        Logger.getLogger(GUIVenda.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                    if (qualAcao.equals("incluir")) {
                        daoVenda.inserir(venda);
                    } else {
                        daoVenda.atualizar(venda);
                    }
                    tfIdVenda.setEditable(true);
                    tfIdVenda.requestFocus();
                    tfDataVenda.setText("");
                    btnSave.setVisible(false);
                    pnNorte.setBackground(Color.green);
                } catch (Exception erro) {
                    jTextArea.append("Erro............");
                    tfIdVenda.setEditable(true);
                    pnNorte.setBackground(Color.red);
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnUpdate.setVisible(false);
                btnDelete.setVisible(false);
                tfDataVenda.requestFocus();
                btnSave.setVisible(true);
                qualAcao = "editar";
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,
                        "Confirma a exclusão do registro <ID = " + venda.getIdVenda() + ">?", "Confirm",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                    daoVenda.remover(venda);
                    tfIdVenda.requestFocus();
                    tfDataVenda.setText("");
                    tfIdVenda.setEditable(true);
                    btnUpdate.setVisible(false);
                    btnDelete.setVisible(false);
                }
            }
        });
        btnList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUIListagemVenda guiListagem = new GUIListagemVenda(daoVenda.list());
            }
        });
        
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        tfDataVenda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    jTextArea.setText("");
                    DateChooser dc1 = new DateChooser((JFrame) null, "Escolha uma data");
                    data1 = dc1.select();
                    tfDataVenda.setText(sdf.format(data1));
                } catch (Exception ex) {
                    jTextArea.setText("Escolha uma data\n");
                }
            }
        });
        tfIdVenda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> listaAuxiliar = daoVenda.listInOrderNomeStrings("id");
                if (listaAuxiliar.size() > 0) {
                    String selectedItem = new JanelaPesquisar(listaAuxiliar).getValorRetornado();
                    if (!selectedItem.equals("")) {
                        String[] aux = selectedItem.split("-");
                        tfIdVenda.setText(aux[0]);
                        btnRetrieve.doClick();

                    } else {
                        tfIdVenda.requestFocus();
                        tfIdVenda.selectAll();
                    }
                }
            }
        });
        CentroDoMonitorMaior centroDoMonitorMaior = new CentroDoMonitorMaior();
        setLocation(centroDoMonitorMaior.getCentroMonitorMaior(this));
        setVisible(true);
        
        
        
        
        JPanel pnTeste = new JPanel();
        pnCentroMain.add(pnTeste);
        

        List< VendaHasProduto> lista = new ArrayList<>();
        tableModel = new VendaHasProdutoTableModel(lista);
        table.setModel(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        pnTeste.add(scrollPane);

       

        table.setDefaultEditor(Date.class, new DateEditor());
        table.setDefaultRenderer(Date.class, new DateRenderer());

        // É necessário clicar antes na tabela para o código funcionar
        InputMap im = table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap actionMap = table.getActionMap();

//        KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0);
//        im.put(enterKey, "Action.insert");
//
//        actionMap.put("Action.insert", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent evt) {
//                btnAdd.doClick();
//                
//            }
//        });

//---------------------------------- button delete -----------------------------
        KeyStroke delKey = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0);
        im.put(delKey, "Action.delete");

        actionMap.put("Action.delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (table.getSelectedRow() >= 0) {

                    if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,
                            "Confirma a exclusão da Itenspedido [" + tableModel.getValue(table.getSelectedRow()).getVendaHasProdutoPK().getProdutoIdProduto()+ " - "
                            + tableModel.getValue(table.getSelectedRow()).getVendaHasProdutoPK().getProdutoIdProduto()+ "]?", "Confirm",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
                        btnRem.doClick();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Escolha na tabela a VendaHasProduto a ser excluída");
                }
            }
        });

//========================================== fechar a janela ============================================
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                dispose();
            }
        });
//========================================== botão add ============================================

                btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VendaHasProduto vendaHasProduto = new VendaHasProduto();
                VendaHasProdutoPK vendaHasProdutoPK = new VendaHasProdutoPK();
                vendaHasProduto.setVendaHasProdutoPK(vendaHasProdutoPK);
               
                
                
                vendaHasProdutoPK.setProdutoIdProduto(1);
                vendaHasProdutoPK.setVendaIdVenda(1);
                vendaHasProduto.setQuantidadeProduto(0);
                vendaHasProduto.setPrecoVenda(0.0);
                
                try {
                    VendaHasProduto v = daoItensPedido.obter(vendaHasProduto.getVendaHasProdutoPK());
                    if (v == null) {
                        daoItensPedido.inserir(vendaHasProduto);
                        tableModel.onAdd(vendaHasProduto);
                        tableModel.fireTableDataChanged();
                    } else {
                        JOptionPane.showMessageDialog(null, "Já existe essa conta");
                    }
                } catch (Exception err) {
                    JOptionPane.showMessageDialog(null,"Erro ao inserir a conta =>" +
                            vendaHasProduto.getVendaHasProdutoPK().getProdutoIdProduto()+
                            vendaHasProduto.getVendaHasProdutoPK().getVendaIdVenda() +
                            " com o erro=>" + err.getMessage());
                }
                
//                        daoItensPedido.inserir(vendaHasProduto);
//                        tableModel.onAdd(vendaHasProduto);
//                        tableModel.fireTableDataChanged();
            }
        });//============================================ botao remover =======================================================

        btnRem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() != -1 && table.getSelectedRow() < tableModel.getRowCount()) {
                    VendaHasProduto VendaHasProduto = tableModel.getValue(table.getSelectedRow());
                    daoItensPedido.remover(VendaHasProduto);
                    tableModel.onRemove(table.getSelectedRows());

                } else {
                    JOptionPane.showMessageDialog(null, "Escolha na tabela a conta a ser excluída");
                    table.requestFocus();
                }
                tableModel.fireTableDataChanged();
            }
        });//============================================ botao carregar =======================================================

        btnCarregar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
            }

        });
//============================================ listener table =======================================================

        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                 //if (tableModel.mudou) {
                if (table.getSelectedRow() != -1 && table.getSelectedRow() < tableModel.getRowCount()) {
                    VendaHasProduto c = tableModel.getValue(table.getSelectedRow());
                    daoItensPedido.atualizar(c);
                }
                //}
            }

            
        });//============================================ fim do construtor gui =======================================================

        
        setLocation(centroDoMonitorMaior.getCentroMonitorMaior(this));
        btnCarregar.doClick();//carrega os dados 

        
        setVisible(true);

    } //fim do construtor da GUI


    

//============================================ date render =======================================================
    private static class DateRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!(value instanceof Date)) {
                return this;
            }
            DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
            setText(DATE_FORMAT.format((Date) value));
            return this;
        }
    }

//============================================ date editor =======================================================
    private static class DateEditor extends AbstractCellEditor implements TableCellEditor {

        private static final long serialVersionUID = 1L;
        private final JSpinner theSpinner;
        private Object value;

        DateEditor() {
            theSpinner = new JSpinner(new SpinnerDateModel());
            theSpinner.setOpaque(true);
            theSpinner.setEditor(new JSpinner.DateEditor(theSpinner, "dd/MM/yyyy"));
        }

        @Override
        public Object getCellEditorValue() {
            return theSpinner.getValue();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            theSpinner.setValue(value);
            if (isSelected) {
                theSpinner.setBackground(table.getSelectionBackground());
            } else {
                theSpinner.setBackground(table.getBackground());
            }
            return theSpinner;
        }
    }


    public static void main(String[] args) {
        GUIVenda guiVenda = new GUIVenda();
    }
}
