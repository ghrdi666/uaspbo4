package barang;

import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FormBarang extends JFrame {
    private String[] judul = {"Kode Barang", "Nama Barang", "Harga Barang", "Stok Barang"};
    DefaultTableModel df;
    JTable tab = new JTable();
    JScrollPane scp = new JScrollPane();
    JPanel pnl = new JPanel();
    JLabel lblkode = new JLabel("Kode Barang");
    JTextField txkode = new JTextField(10);
    JLabel lblnama = new JLabel("Nama Barang");
    JTextField txnama = new JTextField(20);
    JLabel lblharga = new JLabel("Harga Barang");
    JTextField txharga = new JTextField(10);
    JLabel lblstok = new JLabel("Stok Barang");
    JTextField txstok = new JTextField(10);
    JButton btadd = new JButton("Simpan");
    JButton btnew = new JButton("Baru");
    JButton btdel = new JButton("Hapus");
    JButton btedit = new JButton("Ubah");

    FormBarang() {
        super("Data Barang");
        setSize(500, 400);
        pnl.setLayout(null);
        pnl.add(lblkode);
        lblkode.setBounds(20, 10, 100, 20);
        pnl.add(txkode);
        txkode.setBounds(130, 10, 100, 20);
        pnl.add(lblnama);
        lblnama.setBounds(20, 40, 100, 20);
        pnl.add(txnama);
        txnama.setBounds(130, 40, 200, 20);
        pnl.add(lblharga);
        lblharga.setBounds(20, 70, 100, 20);
        pnl.add(txharga);
        txharga.setBounds(130, 70, 100, 20);
        pnl.add(lblstok);
        lblstok.setBounds(20, 100, 100, 20);
        pnl.add(txstok);
        txstok.setBounds(130, 100, 100, 20);

        pnl.add(btnew);
        btnew.setBounds(350, 10, 100, 20);
        btnew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnewAksi(e);
            }
        });
        pnl.add(btadd);
        btadd.setBounds(350, 40, 100, 20);
        btadd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btaddAksi(e);
            }
        });
        pnl.add(btedit);
        btedit.setBounds(350, 70, 100, 20);
        btedit.setEnabled(false);
        btedit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bteditAksi(e);
            }
        });
        pnl.add(btdel);
        btdel.setBounds(350, 100, 100, 20);
        btdel.setEnabled(false);
        btdel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btdelAksi(e);
            }
        });
        df = new DefaultTableModel(null, judul);
        tab.setModel(df);
        scp.getViewport().add(tab);
        tab.setEnabled(true);
        tab.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                tabMouseClicked(evt);
            }
        });
        scp.setBounds(20, 140, 450, 200);
        pnl.add(scp);
        getContentPane().add(pnl);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    void loadData() {
        try {
            Connection cn = new ConnectDB().getConnect();
            Statement st = cn.createStatement();
            String sql = "SELECT * FROM tbl_barang";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String kode_barang, nama_barang;
                int harga_barang, stok_barang;
                kode_barang = rs.getString("kode_barang");
                nama_barang = rs.getString("nama_barang");
                harga_barang = rs.getInt("harga_barang");
                stok_barang = rs.getInt("stok_barang");
                String[] data = {kode_barang, nama_barang, String.valueOf(harga_barang), String.valueOf(stok_barang)};
                df.addRow(data);
            }
            rs.close();
            cn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void clearTable() {
        int numRow = df.getRowCount();
        for (int i = 0; i < numRow; i++) {
            df.removeRow(0);
        }
    }

    void clearTextField() {
        txkode.setText(null);
        txnama.setText(null);
        txharga.setText(null);
        txstok.setText(null);
    }

    void simpanData(Barang B) {
        try {
            Connection cn = new ConnectDB().getConnect();
            Statement st = cn.createStatement();
            String sql = "INSERT INTO tbl_barang (kode_barang, nama_barang, harga_barang, stok_barang) " +
                    "VALUES ('" + B.getKodeBarang() + "', '" + B.getNamaBarang() + "', " + B.getHargaBarang() + ", " + B.getStokBarang() + ")";
            int result = st.executeUpdate(sql);
            cn.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan",
                    "Info Proses", JOptionPane.INFORMATION_MESSAGE);
            String[] data = {B.getKodeBarang(), B.getNamaBarang(), String.valueOf(B.getHargaBarang()), String.valueOf(B.getStokBarang())};
            df.addRow(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void hapusData(String kode_barang) {
        try {
            Connection cn = new ConnectDB().getConnect();
            Statement st = cn.createStatement();
            String sql = "DELETE FROM tbl_barang WHERE kode_barang = '" + kode_barang + "'";
            int result = st.executeUpdate(sql);
            cn.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus", "Info Proses",
                    JOptionPane.INFORMATION_MESSAGE);
            df.removeRow(tab.getSelectedRow());
            clearTextField();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void ubahData(Barang B, String kode_barang) {
        try {
            Connection cn = new ConnectDB().getConnect();
            Statement st = cn.createStatement();
            String sql = "UPDATE tbl_barang SET kode_barang='" + B.getKodeBarang() + "', nama_barang='" + B.getNamaBarang() + "', harga_barang=" + B.getHargaBarang() + ", stok_barang=" + B.getStokBarang() + " WHERE kode_barang='" + kode_barang + "'";
            int result = st.executeUpdate(sql);
            cn.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah", "Info Proses",
                    JOptionPane.INFORMATION_MESSAGE);
            clearTable();
            loadData();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void btnewAksi(ActionEvent evt) {
        clearTextField();
        btedit.setEnabled(false);
        btdel.setEnabled(false);
        btadd.setEnabled(true);
    }

    private void btaddAksi(ActionEvent evt) {
        Barang B = new Barang();
        B.setKodeBarang(txkode.getText());
        B.setNamaBarang(txnama.getText());
        B.setHargaBarang(Integer.parseInt(txharga.getText()));
        B.setStokBarang(Integer.parseInt(txstok.getText()));
        simpanData(B);
    }

    private void btdelAksi(ActionEvent evt) {
        int status;
        status = JOptionPane.showConfirmDialog(null, "Yakin data akan dihapus?",
                "Konfirmasi", JOptionPane.OK_CANCEL_OPTION);
        if (status == 0) {
            hapusData(txkode.getText());
        }
    }

    private void bteditAksi(ActionEvent evt) {
        Barang B = new Barang();
        B.setKodeBarang(txkode.getText());
        B.setNamaBarang(txnama.getText());
        B.setHargaBarang(Integer.parseInt(txharga.getText()));
        B.setStokBarang(Integer.parseInt(txstok.getText()));
        ubahData(B, tab.getValueAt(tab.getSelectedRow(), 0).toString());
    }

    private void tabMouseClicked(MouseEvent evt) {
        btedit.setEnabled(true);
        btdel.setEnabled(true);
        btadd.setEnabled(false);
        String kode_barang, nama_barang;
        int harga_barang, stok_barang;
        kode_barang = tab.getValueAt(tab.getSelectedRow(), 0).toString();
        nama_barang = tab.getValueAt(tab.getSelectedRow(), 1).toString();
        harga_barang = Integer.parseInt(tab.getValueAt(tab.getSelectedRow(), 2).toString());
        stok_barang = Integer.parseInt(tab.getValueAt(tab.getSelectedRow(), 3).toString());
        txkode.setText(kode_barang);
        txnama.setText(nama_barang);
        txharga.setText(String.valueOf(harga_barang));
        txstok.setText(String.valueOf(stok_barang));
    }

    public static void main(String[] args) {
        new FormBarang().loadData();
    }
}
