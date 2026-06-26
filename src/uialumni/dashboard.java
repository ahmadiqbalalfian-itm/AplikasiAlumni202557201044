/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package uialumni;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.CardLayout;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
//jurusan, guru, kelas
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
//khususon siswa
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.File;
import java.nio.file.Files;

/**
 *
 * @author ACER
 */
public class dashboard extends javax.swing.JFrame {

    /**
     * Creates new form dashboard
     */
    public dashboard() {
        initComponents();
        reset();

        //jurusan
        load_tabel_jurusan();

        load_tabel_guru();

        load_tabel_kelas();
        comboJurusan();
        comboWali();

        load_tabel_siswa();
        comboKelas();
        
        isiJumlahDataDasbor();
    }

    void reset() {
        //card jurusan
        tKodeJurusan.setText(null);
        tKodeJurusan.setEditable(true);
        tNamaJurusan.setText(null);

        //card guru
        tNamaGuru.setText(null);
        tNIP.setText(null);
        tAlamatGuru.setText(null);
        cJenisKelaminGuru.setSelectedItem(null);

        //cardKelas
        tKodeKelas.setText(null);
        tNamaKelas.setText(null);
        cJurusanKelas.setSelectedItem(null);
        cTingkatanKelas.setSelectedItem(null);
        cWaliKelas.setSelectedItem(null);

        //card siswa
        tNIS.setText(null);
        tNamaSiswa.setText(null);
        tAlamatSiswa.setText(null);
        tNomorHPSiswa.setText(null);
        tTempatLahirSiswa.setText(null);
        tTanggalLahirSiswa.setCalendar(null);

        cJenisKelaminSiswa.setSelectedItem(null);
        cKelasSiswa.setSelectedItem(null);

        tFotoSiswa.setText("Foto");
        tFotoPath.setText(null);
        tFotoPath.setIcon(null);
    }

    private void isiJumlahDataDasbor(){
        Connection con = koneksi.konek();
        
        try {
            String sqlJurusan = "SELECT COUNT(*) AS jumlah FROM jurusan";
            Statement psJurusan = con.createStatement();
            ResultSet rsJurusan = psJurusan.executeQuery(sqlJurusan);
            
            if (rsJurusan.next()) {
                int jumlah = rsJurusan.getInt("jumlah");
                tJumlahJurusan.setText(String.valueOf(jumlah));
            }
            
            String sqlGuru = "SELECT COUNT(*) AS jumlah FROM guru";
            Statement psGuru = con.createStatement();
            ResultSet rsGuru = psGuru.executeQuery(sqlGuru);
            
            if (rsGuru.next()) {
                int jumlah = rsJurusan.getInt("jumlah");
                tJumlahGuru.setText(String.valueOf(jumlah));
            }
            
            String sqlSiswa = "SELECT COUNT(*) AS jumlah FROM siswa";
            Statement psSiswa = con.createStatement();
            ResultSet rsSiswa = psSiswa.executeQuery(sqlSiswa);
            
            if (rsSiswa.next()) {
                int jumlah = rsSiswa.getInt("jumlah");
                tJumlahSiswa.setText(String.valueOf(jumlah));
            }
            
            String sqlKelas = "SELECT COUNT(*) AS jumlah FROM kelas";
            Statement psKelas = con.createStatement();
            ResultSet rsKelas = psKelas.executeQuery(sqlKelas);
            
            if (rsKelas.next()) {
                int jumlah = rsKelas.getInt("jumlah");
                tJumlahKelas.setText(String.valueOf(jumlah));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil jumlah data");
        }
    }
    
    void load_tabel_jurusan() {

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Kode Jurusan");

        model.addColumn("Nama Jurusan");

        String sql = "SELECT * FROM jurusan";

        try {
            Connection con = koneksi.konek();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                String kodeJurusan = rs.getString("kode_jur");

                String namaJurusan = rs.getString("nama_jur");

                Object[] baris = {kodeJurusan, namaJurusan};
                model.addRow(baris);
            }

        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data!");
        }
        tblDataJurusan.setModel(model);
    }

    void load_tabel_guru() {

        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("NIP");
        model.addColumn("Nama Guru");
        model.addColumn("L/P");
        model.addColumn("Alamat");

        String sql = "SELECT * FROM guru";

        try {
            Connection con = koneksi.konek();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                String NIP = rs.getString("nip");

                String namaGuru = rs.getString("nama_guru");
                String jenisKelaminGuru = rs.getString("gender");

                String alamatGuru = rs.getString("alamat");

                Object[] baris = {NIP, namaGuru, jenisKelaminGuru, alamatGuru};
                model.addRow(baris);
            }

        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data!");
        }
        tblDataGuru.setModel(model);
    }

    void load_tabel_kelas() {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Kode Kelas");

        model.addColumn("Nama Kelas");

        model.addColumn("Tingkatan");

        model.addColumn("Jurusan");

        model.addColumn("Wali Kelas");

        String sql = "SELECT k.id_kelas,k.nama_kelas,k.tingkatan,j.nama_jur,g.nama_guru "
                + " FROM kelas k "
                + " LEFT JOIN jurusan j ON k.kode_jur=j.kode_jur "
                + " LEFT JOIN guru g ON k.nip_wali_kelas=g.nip";

        try {
            Connection con = koneksi.konek();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                String kodeKelas = rs.getString("id_kelas");
                String namaKelas = rs.getString("nama_kelas");
                String tingkatan = rs.getString("tingkatan");
                String jurusan = rs.getString("nama_jur");
                String waliKelas = rs.getString("nama_guru");

                Object[] baris = {kodeKelas, namaKelas, tingkatan, jurusan, waliKelas};
                model.addRow(baris);
            }

        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data!");
        }
        tblDataKelas.setModel(model);
    }

    void load_tabel_siswa() {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("NIS");

        model.addColumn("Nama Siswa");

        model.addColumn("L/P");

        model.addColumn("Tempat Lahir");

        model.addColumn("Tgl Lahir");

        model.addColumn("Kelas");

        model.addColumn("HP");

        String sql = "SELECT * FROM siswa";

        try {
            Connection con = koneksi.konek();

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                String nis = rs.getString("nis");
                String namaSiswa = rs.getString("nama_siswa");
                String jenisKelamin = rs.getString("gender");
                String tempatLahir = rs.getString("tempat_lahir");
                String tglLahir = rs.getString("tgl_lahir");
                String kelas = rs.getString("id_kelas");
                String hp = rs.getString("no_hp");

                Object[] baris = {nis, namaSiswa, jenisKelamin, tempatLahir, tglLahir, kelas, hp};
                model.addRow(baris);
            }

        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data!");
        }
        tblDataSiswa.setModel(model);
    }

    private void pindahKartu(String namaKartu) {
        CardLayout cl = (CardLayout) pnlContent.getLayout();
        cl.show(pnlContent, namaKartu);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlSideBar = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnDasbor = new javax.swing.JButton();
        btnJurusan = new javax.swing.JButton();
        btnKelas = new javax.swing.JButton();
        btnGuru = new javax.swing.JButton();
        btnAbout = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnSiswa = new javax.swing.JButton();
        pnlContent = new javax.swing.JPanel();
        cardDasbor = new javax.swing.JPanel();
        bawah = new javax.swing.JPanel();
        Jurusan = new javax.swing.JPanel();
        titleJurusan = new javax.swing.JPanel();
        tTittle1 = new javax.swing.JLabel();
        AngkaJurusan = new javax.swing.JPanel();
        tJumlahJurusan = new javax.swing.JLabel();
        Guru = new javax.swing.JPanel();
        titleGuru = new javax.swing.JPanel();
        tTittle = new javax.swing.JLabel();
        angkaGuru = new javax.swing.JPanel();
        tJumlahGuru = new javax.swing.JLabel();
        Siswa = new javax.swing.JPanel();
        tittleSiswa = new javax.swing.JPanel();
        tTittle2 = new javax.swing.JLabel();
        angkaSiswa = new javax.swing.JPanel();
        tJumlahSiswa = new javax.swing.JLabel();
        Kelas = new javax.swing.JPanel();
        titleKelas = new javax.swing.JPanel();
        tTittle3 = new javax.swing.JLabel();
        angkaKelas = new javax.swing.JPanel();
        tJumlahKelas = new javax.swing.JLabel();
        cardJurusan = new javax.swing.JPanel();
        atasJurusan = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        bawahJurusan = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        tKodeJurusan = new javax.swing.JTextField();
        tNamaJurusan = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnTambahJurusan = new javax.swing.JButton();
        btnUbahJurusan = new javax.swing.JButton();
        btnHapusJurusan = new javax.swing.JButton();
        btnResetJurusan = new javax.swing.JButton();
        scrlDataJurusan = new javax.swing.JScrollPane();
        tblDataJurusan = new javax.swing.JTable();
        cardGuru = new javax.swing.JPanel();
        atasGuru = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        kontenGuru = new javax.swing.JPanel();
        pnlInput = new javax.swing.JPanel();
        tNIP = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tNamaGuru = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cJenisKelaminGuru = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        tAlamatGuru = new javax.swing.JTextArea();
        pnlOutput = new javax.swing.JPanel();
        scrlDataGuru = new javax.swing.JScrollPane();
        tblDataGuru = new javax.swing.JTable();
        tombolGuru = new javax.swing.JPanel();
        btnTambahGuru = new javax.swing.JButton();
        btnUbahGuru = new javax.swing.JButton();
        btnHapusGuru = new javax.swing.JButton();
        btnResetGuru = new javax.swing.JButton();
        cardKelas = new javax.swing.JPanel();
        atasKelas = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        kontenKelas = new javax.swing.JPanel();
        pnlInputKelas = new javax.swing.JPanel();
        tKodeKelas = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        tNamaKelas = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        cTingkatanKelas = new javax.swing.JComboBox<>();
        cJurusanKelas = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        cWaliKelas = new javax.swing.JComboBox<>();
        pnlOutputKelas = new javax.swing.JPanel();
        scrlDataKelas = new javax.swing.JScrollPane();
        tblDataKelas = new javax.swing.JTable();
        tombolKelas = new javax.swing.JPanel();
        btnTambahKelas = new javax.swing.JButton();
        btnUbahKelas = new javax.swing.JButton();
        btnHapusKelas = new javax.swing.JButton();
        btnResetKelas = new javax.swing.JButton();
        cardSiswa = new javax.swing.JPanel();
        atasSiswa = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        kontenSiswa = new javax.swing.JPanel();
        pnlFotoSiswa = new javax.swing.JPanel();
        tFotoSiswa = new javax.swing.JLabel();
        pnlInputSiswa1 = new javax.swing.JPanel();
        tNIS = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        tNamaSiswa = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        cJenisKelaminSiswa = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        tTempatLahirSiswa = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        tTanggalLahirSiswa = new com.toedter.calendar.JDateChooser();
        pnlInputSiswa2 = new javax.swing.JPanel();
        tNomorHPSiswa = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        cKelasSiswa = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        scrAlamatSiswa = new javax.swing.JScrollPane();
        tAlamatSiswa = new javax.swing.JTextArea();
        pnlBawahSiswa = new javax.swing.JPanel();
        tombolSiswa = new javax.swing.JPanel();
        btnTambahSiswa = new javax.swing.JButton();
        btnUbahSiswa = new javax.swing.JButton();
        btnHapusSiswa = new javax.swing.JButton();
        btnResetSiswa = new javax.swing.JButton();
        pnlOutputSiswa = new javax.swing.JPanel();
        scrDataSiswa = new javax.swing.JScrollPane();
        tblDataSiswa = new javax.swing.JTable();
        tFotoPath = new javax.swing.JLabel();
        cardAbout = new javax.swing.JPanel();
        atasAboutUs = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        bawahJurusan1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlSideBar.setBackground(new java.awt.Color(153, 204, 255));
        pnlSideBar.setMinimumSize(new java.awt.Dimension(200, 100));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-graduation-40.png"))); // NOI18N
        jLabel4.setText("Aloomni");

        btnDasbor.setBackground(new java.awt.Color(153, 204, 255));
        btnDasbor.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnDasbor.setForeground(new java.awt.Color(255, 255, 255));
        btnDasbor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-compass-20.png"))); // NOI18N
        btnDasbor.setText("Dasbor");
        btnDasbor.setBorder(null);
        btnDasbor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDasbor.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDasbor.setIconTextGap(10);
        btnDasbor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDasborActionPerformed(evt);
            }
        });

        btnJurusan.setBackground(new java.awt.Color(153, 204, 255));
        btnJurusan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnJurusan.setForeground(new java.awt.Color(255, 255, 255));
        btnJurusan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-library-20.png"))); // NOI18N
        btnJurusan.setText("Jurusan");
        btnJurusan.setBorder(null);
        btnJurusan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnJurusan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnJurusan.setIconTextGap(10);
        btnJurusan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJurusanActionPerformed(evt);
            }
        });

        btnKelas.setBackground(new java.awt.Color(153, 204, 255));
        btnKelas.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnKelas.setForeground(new java.awt.Color(255, 255, 255));
        btnKelas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-class-20.png"))); // NOI18N
        btnKelas.setText("Kelas");
        btnKelas.setBorder(null);
        btnKelas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnKelas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnKelas.setIconTextGap(10);
        btnKelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKelasActionPerformed(evt);
            }
        });

        btnGuru.setBackground(new java.awt.Color(153, 204, 255));
        btnGuru.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnGuru.setForeground(new java.awt.Color(255, 255, 255));
        btnGuru.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-teacher-20.png"))); // NOI18N
        btnGuru.setText("Guru");
        btnGuru.setBorder(null);
        btnGuru.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuru.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnGuru.setIconTextGap(10);
        btnGuru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuruActionPerformed(evt);
            }
        });

        btnAbout.setBackground(new java.awt.Color(153, 204, 255));
        btnAbout.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnAbout.setForeground(new java.awt.Color(255, 255, 255));
        btnAbout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-about-20.png"))); // NOI18N
        btnAbout.setText("About");
        btnAbout.setBorder(null);
        btnAbout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAbout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAbout.setIconTextGap(10);
        btnAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAboutActionPerformed(evt);
            }
        });

        btnLogout.setBackground(new java.awt.Color(153, 204, 255));
        btnLogout.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-logout-20.png"))); // NOI18N
        btnLogout.setText("Logout");
        btnLogout.setBorder(null);
        btnLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLogout.setIconTextGap(10);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        btnSiswa.setBackground(new java.awt.Color(153, 204, 255));
        btnSiswa.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnSiswa.setForeground(new java.awt.Color(255, 255, 255));
        btnSiswa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-person-20.png"))); // NOI18N
        btnSiswa.setText("Siswa");
        btnSiswa.setBorder(null);
        btnSiswa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSiswa.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSiswa.setIconTextGap(10);
        btnSiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiswaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSideBarLayout = new javax.swing.GroupLayout(pnlSideBar);
        pnlSideBar.setLayout(pnlSideBarLayout);
        pnlSideBarLayout.setHorizontalGroup(
            pnlSideBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
            .addGroup(pnlSideBarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSideBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDasbor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnJurusan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnKelas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnGuru, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAbout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLogout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSiswa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        pnlSideBarLayout.setVerticalGroup(
            pnlSideBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSideBarLayout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDasbor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnJurusan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnGuru)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnKelas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSiswa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAbout)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLogout)
                .addGap(0, 368, Short.MAX_VALUE))
        );

        getContentPane().add(pnlSideBar, java.awt.BorderLayout.LINE_START);

        pnlContent.setBackground(new java.awt.Color(255, 255, 255));
        pnlContent.setLayout(new java.awt.CardLayout());

        cardDasbor.setBackground(new java.awt.Color(255, 255, 255));
        cardDasbor.setLayout(new java.awt.BorderLayout());

        bawah.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 20));
        bawah.setLayout(new java.awt.GridLayout(2, 1));

        Jurusan.setLayout(new java.awt.BorderLayout());

        titleJurusan.setBackground(new java.awt.Color(153, 204, 255));

        tTittle1.setBackground(new java.awt.Color(153, 204, 255));
        tTittle1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tTittle1.setForeground(new java.awt.Color(255, 255, 255));
        tTittle1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tTittle1.setText("Jumlah Jurusan");

        javax.swing.GroupLayout titleJurusanLayout = new javax.swing.GroupLayout(titleJurusan);
        titleJurusan.setLayout(titleJurusanLayout);
        titleJurusanLayout.setHorizontalGroup(
            titleJurusanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tTittle1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
        );
        titleJurusanLayout.setVerticalGroup(
            titleJurusanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tTittle1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
        );

        Jurusan.add(titleJurusan, java.awt.BorderLayout.PAGE_START);

        AngkaJurusan.setBackground(new java.awt.Color(255, 255, 255));

        tJumlahJurusan.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tJumlahJurusan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tJumlahJurusan.setText("14");

        javax.swing.GroupLayout AngkaJurusanLayout = new javax.swing.GroupLayout(AngkaJurusan);
        AngkaJurusan.setLayout(AngkaJurusanLayout);
        AngkaJurusanLayout.setHorizontalGroup(
            AngkaJurusanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tJumlahJurusan, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
        );
        AngkaJurusanLayout.setVerticalGroup(
            AngkaJurusanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tJumlahJurusan, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
        );

        Jurusan.add(AngkaJurusan, java.awt.BorderLayout.CENTER);

        bawah.add(Jurusan);

        Guru.setLayout(new java.awt.BorderLayout());

        titleGuru.setBackground(new java.awt.Color(153, 204, 255));

        tTittle.setBackground(new java.awt.Color(153, 204, 255));
        tTittle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tTittle.setForeground(new java.awt.Color(255, 255, 255));
        tTittle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tTittle.setText("Jumlah Guru");

        javax.swing.GroupLayout titleGuruLayout = new javax.swing.GroupLayout(titleGuru);
        titleGuru.setLayout(titleGuruLayout);
        titleGuruLayout.setHorizontalGroup(
            titleGuruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tTittle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
        );
        titleGuruLayout.setVerticalGroup(
            titleGuruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tTittle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
        );

        Guru.add(titleGuru, java.awt.BorderLayout.PAGE_START);

        angkaGuru.setBackground(new java.awt.Color(255, 255, 255));

        tJumlahGuru.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tJumlahGuru.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tJumlahGuru.setText("3");

        javax.swing.GroupLayout angkaGuruLayout = new javax.swing.GroupLayout(angkaGuru);
        angkaGuru.setLayout(angkaGuruLayout);
        angkaGuruLayout.setHorizontalGroup(
            angkaGuruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tJumlahGuru, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
        );
        angkaGuruLayout.setVerticalGroup(
            angkaGuruLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tJumlahGuru, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
        );

        Guru.add(angkaGuru, java.awt.BorderLayout.CENTER);

        bawah.add(Guru);

        Siswa.setLayout(new java.awt.BorderLayout());

        tittleSiswa.setBackground(new java.awt.Color(153, 204, 255));

        tTittle2.setBackground(new java.awt.Color(153, 204, 255));
        tTittle2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tTittle2.setForeground(new java.awt.Color(255, 255, 255));
        tTittle2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tTittle2.setText("Jumlah Siswa");

        javax.swing.GroupLayout tittleSiswaLayout = new javax.swing.GroupLayout(tittleSiswa);
        tittleSiswa.setLayout(tittleSiswaLayout);
        tittleSiswaLayout.setHorizontalGroup(
            tittleSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tTittle2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
        );
        tittleSiswaLayout.setVerticalGroup(
            tittleSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tTittle2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
        );

        Siswa.add(tittleSiswa, java.awt.BorderLayout.PAGE_START);

        angkaSiswa.setBackground(new java.awt.Color(255, 255, 255));

        tJumlahSiswa.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tJumlahSiswa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tJumlahSiswa.setText("2000");

        javax.swing.GroupLayout angkaSiswaLayout = new javax.swing.GroupLayout(angkaSiswa);
        angkaSiswa.setLayout(angkaSiswaLayout);
        angkaSiswaLayout.setHorizontalGroup(
            angkaSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tJumlahSiswa, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
        );
        angkaSiswaLayout.setVerticalGroup(
            angkaSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tJumlahSiswa, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
        );

        Siswa.add(angkaSiswa, java.awt.BorderLayout.CENTER);

        bawah.add(Siswa);

        Kelas.setLayout(new java.awt.BorderLayout());

        titleKelas.setBackground(new java.awt.Color(153, 204, 255));

        tTittle3.setBackground(new java.awt.Color(153, 204, 255));
        tTittle3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tTittle3.setForeground(new java.awt.Color(255, 255, 255));
        tTittle3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tTittle3.setText("Jumlah Kelas");

        javax.swing.GroupLayout titleKelasLayout = new javax.swing.GroupLayout(titleKelas);
        titleKelas.setLayout(titleKelasLayout);
        titleKelasLayout.setHorizontalGroup(
            titleKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tTittle3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
        );
        titleKelasLayout.setVerticalGroup(
            titleKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tTittle3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
        );

        Kelas.add(titleKelas, java.awt.BorderLayout.PAGE_START);

        angkaKelas.setBackground(new java.awt.Color(255, 255, 255));

        tJumlahKelas.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        tJumlahKelas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tJumlahKelas.setText("7");

        javax.swing.GroupLayout angkaKelasLayout = new javax.swing.GroupLayout(angkaKelas);
        angkaKelas.setLayout(angkaKelasLayout);
        angkaKelasLayout.setHorizontalGroup(
            angkaKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tJumlahKelas, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
        );
        angkaKelasLayout.setVerticalGroup(
            angkaKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tJumlahKelas, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
        );

        Kelas.add(angkaKelas, java.awt.BorderLayout.CENTER);

        bawah.add(Kelas);

        cardDasbor.add(bawah, java.awt.BorderLayout.CENTER);

        pnlContent.add(cardDasbor, "cardDasbor");

        cardJurusan.setBackground(new java.awt.Color(255, 255, 255));
        cardJurusan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 20));
        cardJurusan.setLayout(new java.awt.BorderLayout());

        atasJurusan.setBackground(new java.awt.Color(153, 204, 255));
        atasJurusan.setMaximumSize(new java.awt.Dimension(32767, 30));
        atasJurusan.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Data Jurusan");
        atasJurusan.add(jLabel1, java.awt.BorderLayout.LINE_START);

        cardJurusan.add(atasJurusan, java.awt.BorderLayout.PAGE_START);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Kode Jurusan");

        tKodeJurusan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tKodeJurusan.setText("jTextField1");

        tNamaJurusan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tNamaJurusan.setText("jTextField1");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Nama Jurusan");

        btnTambahJurusan.setBackground(new java.awt.Color(0, 153, 51));
        btnTambahJurusan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTambahJurusan.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahJurusan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-white-plus-20.png"))); // NOI18N
        btnTambahJurusan.setText("Tambah");
        btnTambahJurusan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTambahJurusan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahJurusanActionPerformed(evt);
            }
        });

        btnUbahJurusan.setBackground(new java.awt.Color(255, 153, 51));
        btnUbahJurusan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUbahJurusan.setForeground(new java.awt.Color(255, 255, 255));
        btnUbahJurusan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-white-edit-20.png"))); // NOI18N
        btnUbahJurusan.setText("Ubah");
        btnUbahJurusan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUbahJurusan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahJurusanActionPerformed(evt);
            }
        });

        btnHapusJurusan.setBackground(new java.awt.Color(255, 0, 0));
        btnHapusJurusan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHapusJurusan.setForeground(new java.awt.Color(255, 255, 255));
        btnHapusJurusan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-remove-20.png"))); // NOI18N
        btnHapusJurusan.setText("Hapus");
        btnHapusJurusan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapusJurusan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusJurusanActionPerformed(evt);
            }
        });

        btnResetJurusan.setBackground(new java.awt.Color(51, 102, 255));
        btnResetJurusan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnResetJurusan.setForeground(new java.awt.Color(255, 255, 255));
        btnResetJurusan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-white-reset-20 (1).png"))); // NOI18N
        btnResetJurusan.setText("Reset");
        btnResetJurusan.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnResetJurusan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetJurusanActionPerformed(evt);
            }
        });

        tblDataJurusan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblDataJurusan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataJurusanMouseClicked(evt);
            }
        });
        scrlDataJurusan.setViewportView(tblDataJurusan);

        javax.swing.GroupLayout bawahJurusanLayout = new javax.swing.GroupLayout(bawahJurusan);
        bawahJurusan.setLayout(bawahJurusanLayout);
        bawahJurusanLayout.setHorizontalGroup(
            bawahJurusanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bawahJurusanLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(bawahJurusanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bawahJurusanLayout.createSequentialGroup()
                        .addComponent(scrlDataJurusan)
                        .addContainerGap())
                    .addGroup(bawahJurusanLayout.createSequentialGroup()
                        .addGroup(bawahJurusanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tKodeJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(bawahJurusanLayout.createSequentialGroup()
                                .addComponent(btnTambahJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(68, 68, 68)
                                .addComponent(btnUbahJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(bawahJurusanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(bawahJurusanLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(bawahJurusanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tNamaJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(bawahJurusanLayout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addComponent(btnHapusJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(78, 78, 78)
                                .addComponent(btnResetJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(103, 103, 103))))
        );
        bawahJurusanLayout.setVerticalGroup(
            bawahJurusanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bawahJurusanLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(bawahJurusanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(bawahJurusanLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tNamaJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bawahJurusanLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tKodeJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(bawahJurusanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambahJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUbahJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapusJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnResetJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(scrlDataJurusan, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cardJurusan.add(bawahJurusan, java.awt.BorderLayout.CENTER);

        pnlContent.add(cardJurusan, "cardJurusan");

        cardGuru.setBackground(new java.awt.Color(255, 255, 255));
        cardGuru.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 20));
        cardGuru.setLayout(new java.awt.BorderLayout());

        atasGuru.setBackground(new java.awt.Color(153, 204, 255));
        atasGuru.setMaximumSize(new java.awt.Dimension(32767, 30));
        atasGuru.setLayout(new java.awt.BorderLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Data Guru");
        atasGuru.add(jLabel5, java.awt.BorderLayout.LINE_START);

        cardGuru.add(atasGuru, java.awt.BorderLayout.PAGE_START);

        kontenGuru.setMaximumSize(new java.awt.Dimension(65534, 400));
        kontenGuru.setMinimumSize(new java.awt.Dimension(0, 400));
        kontenGuru.setPreferredSize(new java.awt.Dimension(804, 400));
        kontenGuru.setLayout(new javax.swing.BoxLayout(kontenGuru, javax.swing.BoxLayout.LINE_AXIS));

        pnlInput.setLayout(new java.awt.GridBagLayout());

        tNIP.setText("jTextField1");
        tNIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tNIPActionPerformed(evt);
            }
        });
        tNIP.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tNIPKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 264;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 7);
        pnlInput.add(tNIP, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("NIP");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 303;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 7);
        pnlInput.add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Nama");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 288;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 6, 0, 7);
        pnlInput.add(jLabel7, gridBagConstraints);

        tNamaGuru.setText("jTextField1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 264;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 7);
        pnlInput.add(tNamaGuru, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Jenis Kelamin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 235;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 6, 0, 7);
        pnlInput.add(jLabel8, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Alamat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 279;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 6, 0, 7);
        pnlInput.add(jLabel9, gridBagConstraints);

        cJenisKelaminGuru.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Laki-Laki", "Perempuan" }));
        cJenisKelaminGuru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cJenisKelaminGuruActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 234;
        gridBagConstraints.ipady = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 7);
        pnlInput.add(cJenisKelaminGuru, gridBagConstraints);

        tAlamatGuru.setColumns(20);
        tAlamatGuru.setRows(5);
        jScrollPane3.setViewportView(tAlamatGuru);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 312;
        gridBagConstraints.ipady = 150;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 7);
        pnlInput.add(jScrollPane3, gridBagConstraints);

        kontenGuru.add(pnlInput);

        pnlOutput.setLayout(new java.awt.GridBagLayout());

        tblDataGuru.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblDataGuru.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataGuruMouseClicked(evt);
            }
        });
        scrlDataGuru.setViewportView(tblDataGuru);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 436;
        gridBagConstraints.ipady = 393;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 6, 6, 6);
        pnlOutput.add(scrlDataGuru, gridBagConstraints);

        kontenGuru.add(pnlOutput);

        cardGuru.add(kontenGuru, java.awt.BorderLayout.CENTER);

        tombolGuru.setMinimumSize(new java.awt.Dimension(100, 70));
        tombolGuru.setPreferredSize(new java.awt.Dimension(806, 50));
        tombolGuru.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

        btnTambahGuru.setBackground(new java.awt.Color(0, 153, 51));
        btnTambahGuru.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTambahGuru.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahGuru.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-white-plus-20.png"))); // NOI18N
        btnTambahGuru.setText("Tambah");
        btnTambahGuru.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTambahGuru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahGuruActionPerformed(evt);
            }
        });
        tombolGuru.add(btnTambahGuru);

        btnUbahGuru.setBackground(new java.awt.Color(255, 153, 51));
        btnUbahGuru.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUbahGuru.setForeground(new java.awt.Color(255, 255, 255));
        btnUbahGuru.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-white-edit-20.png"))); // NOI18N
        btnUbahGuru.setText("Ubah");
        btnUbahGuru.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUbahGuru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahGuruActionPerformed(evt);
            }
        });
        tombolGuru.add(btnUbahGuru);

        btnHapusGuru.setBackground(new java.awt.Color(255, 0, 0));
        btnHapusGuru.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHapusGuru.setForeground(new java.awt.Color(255, 255, 255));
        btnHapusGuru.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-remove-20.png"))); // NOI18N
        btnHapusGuru.setText("Hapus");
        btnHapusGuru.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapusGuru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusGuruActionPerformed(evt);
            }
        });
        tombolGuru.add(btnHapusGuru);

        btnResetGuru.setBackground(new java.awt.Color(51, 102, 255));
        btnResetGuru.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnResetGuru.setForeground(new java.awt.Color(255, 255, 255));
        btnResetGuru.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-white-reset-20 (1).png"))); // NOI18N
        btnResetGuru.setText("Reset");
        btnResetGuru.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnResetGuru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetGuruActionPerformed(evt);
            }
        });
        tombolGuru.add(btnResetGuru);

        cardGuru.add(tombolGuru, java.awt.BorderLayout.PAGE_END);

        pnlContent.add(cardGuru, "cardGuru");

        cardKelas.setBackground(new java.awt.Color(255, 255, 255));
        cardKelas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 20));
        cardKelas.setLayout(new java.awt.BorderLayout());

        atasKelas.setBackground(new java.awt.Color(153, 204, 255));
        atasKelas.setMaximumSize(new java.awt.Dimension(32767, 30));
        atasKelas.setLayout(new java.awt.BorderLayout());

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Data Kelas");
        atasKelas.add(jLabel15, java.awt.BorderLayout.LINE_START);

        cardKelas.add(atasKelas, java.awt.BorderLayout.PAGE_START);

        kontenKelas.setLayout(new javax.swing.BoxLayout(kontenKelas, javax.swing.BoxLayout.LINE_AXIS));

        tKodeKelas.setText("jTextField1");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Kode Kelas");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setText("Nama Kelas");

        tNamaKelas.setText("jTextField1");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setText("Tingkatan");

        cTingkatanKelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "10", "11", "12" }));

        cJurusanKelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "IPA", "IPS", "Bahasa" }));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setText("Jurusan");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setText("Wali Kelas");

        cWaliKelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ahmad Iqbal Alfian" }));

        javax.swing.GroupLayout pnlInputKelasLayout = new javax.swing.GroupLayout(pnlInputKelas);
        pnlInputKelas.setLayout(pnlInputKelasLayout);
        pnlInputKelasLayout.setHorizontalGroup(
            pnlInputKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInputKelasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cTingkatanKelas, 0, 334, Short.MAX_VALUE)
                    .addComponent(tKodeKelas, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tNamaKelas, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cJurusanKelas, 0, 334, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cWaliKelas, 0, 334, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlInputKelasLayout.setVerticalGroup(
            pnlInputKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputKelasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tKodeKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tNamaKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cTingkatanKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cJurusanKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cWaliKelas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(113, Short.MAX_VALUE))
        );

        kontenKelas.add(pnlInputKelas);

        tblDataKelas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblDataKelas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataKelasMouseClicked(evt);
            }
        });
        scrlDataKelas.setViewportView(tblDataKelas);

        javax.swing.GroupLayout pnlOutputKelasLayout = new javax.swing.GroupLayout(pnlOutputKelas);
        pnlOutputKelas.setLayout(pnlOutputKelasLayout);
        pnlOutputKelasLayout.setHorizontalGroup(
            pnlOutputKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOutputKelasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrlDataKelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlOutputKelasLayout.setVerticalGroup(
            pnlOutputKelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOutputKelasLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrlDataKelas, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                .addContainerGap())
        );

        kontenKelas.add(pnlOutputKelas);

        cardKelas.add(kontenKelas, java.awt.BorderLayout.CENTER);

        tombolKelas.setMinimumSize(new java.awt.Dimension(100, 70));
        tombolKelas.setPreferredSize(new java.awt.Dimension(806, 50));
        tombolKelas.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 10));

        btnTambahKelas.setBackground(new java.awt.Color(0, 153, 51));
        btnTambahKelas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTambahKelas.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahKelas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-white-plus-20.png"))); // NOI18N
        btnTambahKelas.setText("Tambah");
        btnTambahKelas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTambahKelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahKelasActionPerformed(evt);
            }
        });
        tombolKelas.add(btnTambahKelas);

        btnUbahKelas.setBackground(new java.awt.Color(255, 153, 51));
        btnUbahKelas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUbahKelas.setForeground(new java.awt.Color(255, 255, 255));
        btnUbahKelas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-white-edit-20.png"))); // NOI18N
        btnUbahKelas.setText("Ubah");
        btnUbahKelas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUbahKelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahKelasActionPerformed(evt);
            }
        });
        tombolKelas.add(btnUbahKelas);

        btnHapusKelas.setBackground(new java.awt.Color(255, 0, 0));
        btnHapusKelas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHapusKelas.setForeground(new java.awt.Color(255, 255, 255));
        btnHapusKelas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-remove-20.png"))); // NOI18N
        btnHapusKelas.setText("Hapus");
        btnHapusKelas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapusKelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusKelasActionPerformed(evt);
            }
        });
        tombolKelas.add(btnHapusKelas);

        btnResetKelas.setBackground(new java.awt.Color(51, 102, 255));
        btnResetKelas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnResetKelas.setForeground(new java.awt.Color(255, 255, 255));
        btnResetKelas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-white-reset-20 (1).png"))); // NOI18N
        btnResetKelas.setText("Reset");
        btnResetKelas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnResetKelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetKelasActionPerformed(evt);
            }
        });
        tombolKelas.add(btnResetKelas);

        cardKelas.add(tombolKelas, java.awt.BorderLayout.PAGE_END);

        pnlContent.add(cardKelas, "cardKelas");

        cardSiswa.setBackground(new java.awt.Color(255, 255, 255));
        cardSiswa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 20));
        cardSiswa.setLayout(new java.awt.BorderLayout());

        atasSiswa.setBackground(new java.awt.Color(153, 204, 255));
        atasSiswa.setMaximumSize(new java.awt.Dimension(32767, 30));
        atasSiswa.setLayout(new java.awt.BorderLayout());

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Data Siswa");
        atasSiswa.add(jLabel19, java.awt.BorderLayout.LINE_START);

        cardSiswa.add(atasSiswa, java.awt.BorderLayout.PAGE_START);

        kontenSiswa.setMaximumSize(new java.awt.Dimension(797, 346));
        kontenSiswa.setPreferredSize(new java.awt.Dimension(797, 346));
        kontenSiswa.setLayout(new javax.swing.BoxLayout(kontenSiswa, javax.swing.BoxLayout.LINE_AXIS));

        tFotoSiswa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tFotoSiswa.setText("Foto");
        tFotoSiswa.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        tFotoSiswa.setMaximumSize(new java.awt.Dimension(150, 200));
        tFotoSiswa.setMinimumSize(new java.awt.Dimension(150, 200));
        tFotoSiswa.setPreferredSize(new java.awt.Dimension(150, 200));
        tFotoSiswa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tFotoSiswaMouseClicked(evt);
            }
        });
        pnlFotoSiswa.add(tFotoSiswa);

        kontenSiswa.add(pnlFotoSiswa);

        tNIS.setText("jTextField1");
        tNIS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tNISKeyTyped(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setText("NIS");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel23.setText("Nama");

        tNamaSiswa.setText("jTextField1");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel24.setText("Jenis Kelamin");

        cJenisKelaminSiswa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "10", "11", "12" }));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel27.setText("Tanggal Lahir");

        tTempatLahirSiswa.setText("jTextField1");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setText("Tempat Lahir");

        tTanggalLahirSiswa.setDateFormatString("yyyy-MM-dd");

        javax.swing.GroupLayout pnlInputSiswa1Layout = new javax.swing.GroupLayout(pnlInputSiswa1);
        pnlInputSiswa1.setLayout(pnlInputSiswa1Layout);
        pnlInputSiswa1Layout.setHorizontalGroup(
            pnlInputSiswa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputSiswa1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputSiswa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tNIS)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tNamaSiswa)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                    .addComponent(cJenisKelaminSiswa, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tTempatLahirSiswa)
                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tTanggalLahirSiswa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlInputSiswa1Layout.setVerticalGroup(
            pnlInputSiswa1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputSiswa1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tNIS, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tNamaSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cJenisKelaminSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tTempatLahirSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tTanggalLahirSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        kontenSiswa.add(pnlInputSiswa1);

        tNomorHPSiswa.setText("jTextField1");
        tNomorHPSiswa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tNomorHPSiswaKeyTyped(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setText("NOMOR HP");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setText("Kelas");

        cKelasSiswa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "10 IPA 1", "11 IPS 2", "12 Bahasa" }));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel31.setText("Alamat");

        tAlamatSiswa.setColumns(20);
        tAlamatSiswa.setRows(5);
        tAlamatSiswa.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        scrAlamatSiswa.setViewportView(tAlamatSiswa);

        javax.swing.GroupLayout pnlInputSiswa2Layout = new javax.swing.GroupLayout(pnlInputSiswa2);
        pnlInputSiswa2.setLayout(pnlInputSiswa2Layout);
        pnlInputSiswa2Layout.setHorizontalGroup(
            pnlInputSiswa2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInputSiswa2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlInputSiswa2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlInputSiswa2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(scrAlamatSiswa))
                    .addComponent(tNomorHPSiswa, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlInputSiswa2Layout.createSequentialGroup()
                        .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                        .addGap(216, 216, 216))
                    .addComponent(cKelasSiswa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlInputSiswa2Layout.setVerticalGroup(
            pnlInputSiswa2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInputSiswa2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tNomorHPSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cKelasSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrAlamatSiswa, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                .addContainerGap())
        );

        kontenSiswa.add(pnlInputSiswa2);

        cardSiswa.add(kontenSiswa, java.awt.BorderLayout.CENTER);

        pnlBawahSiswa.setBackground(new java.awt.Color(255, 255, 255));
        pnlBawahSiswa.setMaximumSize(new java.awt.Dimension(32767, 100));

        tombolSiswa.setMaximumSize(new java.awt.Dimension(32767, 50));
        tombolSiswa.setMinimumSize(new java.awt.Dimension(100, 50));
        tombolSiswa.setPreferredSize(new java.awt.Dimension(806, 50));
        tombolSiswa.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 10));

        btnTambahSiswa.setBackground(new java.awt.Color(0, 153, 51));
        btnTambahSiswa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTambahSiswa.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahSiswa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-white-plus-20.png"))); // NOI18N
        btnTambahSiswa.setText("Tambah");
        btnTambahSiswa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTambahSiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahSiswaActionPerformed(evt);
            }
        });
        tombolSiswa.add(btnTambahSiswa);

        btnUbahSiswa.setBackground(new java.awt.Color(255, 153, 51));
        btnUbahSiswa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUbahSiswa.setForeground(new java.awt.Color(255, 255, 255));
        btnUbahSiswa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-white-edit-20.png"))); // NOI18N
        btnUbahSiswa.setText("Ubah");
        btnUbahSiswa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUbahSiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahSiswaActionPerformed(evt);
            }
        });
        tombolSiswa.add(btnUbahSiswa);

        btnHapusSiswa.setBackground(new java.awt.Color(255, 0, 0));
        btnHapusSiswa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHapusSiswa.setForeground(new java.awt.Color(255, 255, 255));
        btnHapusSiswa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-remove-20.png"))); // NOI18N
        btnHapusSiswa.setText("Hapus");
        btnHapusSiswa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapusSiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusSiswaActionPerformed(evt);
            }
        });
        tombolSiswa.add(btnHapusSiswa);

        btnResetSiswa.setBackground(new java.awt.Color(51, 102, 255));
        btnResetSiswa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnResetSiswa.setForeground(new java.awt.Color(255, 255, 255));
        btnResetSiswa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/uialumni/img/icons8-white-reset-20 (1).png"))); // NOI18N
        btnResetSiswa.setText("Reset");
        btnResetSiswa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnResetSiswa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetSiswaActionPerformed(evt);
            }
        });
        tombolSiswa.add(btnResetSiswa);

        tblDataSiswa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblDataSiswa.setShowGrid(false);
        tblDataSiswa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataSiswaMouseClicked(evt);
            }
        });
        scrDataSiswa.setViewportView(tblDataSiswa);

        tFotoPath.setText("jLabel34");

        javax.swing.GroupLayout pnlOutputSiswaLayout = new javax.swing.GroupLayout(pnlOutputSiswa);
        pnlOutputSiswa.setLayout(pnlOutputSiswaLayout);
        pnlOutputSiswaLayout.setHorizontalGroup(
            pnlOutputSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlOutputSiswaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlOutputSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tFotoPath, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrDataSiswa, javax.swing.GroupLayout.DEFAULT_SIZE, 794, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlOutputSiswaLayout.setVerticalGroup(
            pnlOutputSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOutputSiswaLayout.createSequentialGroup()
                .addComponent(scrDataSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tFotoPath))
        );

        javax.swing.GroupLayout pnlBawahSiswaLayout = new javax.swing.GroupLayout(pnlBawahSiswa);
        pnlBawahSiswa.setLayout(pnlBawahSiswaLayout);
        pnlBawahSiswaLayout.setHorizontalGroup(
            pnlBawahSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBawahSiswaLayout.createSequentialGroup()
                .addComponent(tombolSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(pnlBawahSiswaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlOutputSiswa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlBawahSiswaLayout.setVerticalGroup(
            pnlBawahSiswaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBawahSiswaLayout.createSequentialGroup()
                .addComponent(tombolSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlOutputSiswa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        cardSiswa.add(pnlBawahSiswa, java.awt.BorderLayout.PAGE_END);

        pnlContent.add(cardSiswa, "cardSiswa");

        cardAbout.setBackground(new java.awt.Color(255, 255, 255));
        cardAbout.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 20));
        cardAbout.setLayout(new java.awt.BorderLayout());

        atasAboutUs.setBackground(new java.awt.Color(153, 204, 255));
        atasAboutUs.setMaximumSize(new java.awt.Dimension(32767, 30));
        atasAboutUs.setLayout(new java.awt.BorderLayout());

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("About Us");
        atasAboutUs.add(jLabel10, java.awt.BorderLayout.LINE_START);

        cardAbout.add(atasAboutUs, java.awt.BorderLayout.PAGE_START);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel11.setText("This App is develop by:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel12.setText("Konnichiwaaa...");

        jLabel13.setText("Name");

        jLabel14.setText(": Ahmad Iqbal Alfian");

        jLabel26.setText("Identity");

        jLabel30.setText(": 202557201044");

        jLabel32.setText(": Information System");

        jLabel33.setText("Study Program");

        javax.swing.GroupLayout bawahJurusan1Layout = new javax.swing.GroupLayout(bawahJurusan1);
        bawahJurusan1.setLayout(bawahJurusan1Layout);
        bawahJurusan1Layout.setHorizontalGroup(
            bawahJurusan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bawahJurusan1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(bawahJurusan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bawahJurusan1Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14))
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(bawahJurusan1Layout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30))
                    .addGroup(bawahJurusan1Layout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel32)))
                .addContainerGap(510, Short.MAX_VALUE))
        );
        bawahJurusan1Layout.setVerticalGroup(
            bawahJurusan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bawahJurusan1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bawahJurusan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bawahJurusan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bawahJurusan1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(jLabel32))
                .addContainerGap(430, Short.MAX_VALUE))
        );

        cardAbout.add(bawahJurusan1, java.awt.BorderLayout.CENTER);

        pnlContent.add(cardAbout, "cardAbout");

        getContentPane().add(pnlContent, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        int pilihan = JOptionPane.showConfirmDialog(null,
                "Apakah Anda yakin ingin keluar?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        switch (pilihan) {
            case JOptionPane.YES_OPTION:
                dispose();
                new frameLogin().setVisible(true);
                break;
            case JOptionPane.NO_OPTION:
                break;
            default:
                break;
        }

    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnTambahJurusanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahJurusanActionPerformed
        // TODO add your handling code here:
        String kodeJurusan = tKodeJurusan.getText();

        String namaJurusan = tNamaJurusan.getText();

        String sql = "INSERT INTO jurusan(kode_jur, nama_jur) VALUES (?,?)";

        try {
            Connection con = koneksi.konek();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, kodeJurusan);

            ps.setString(2, namaJurusan);

            ps.execute();

            JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
        } catch (SQLException sQLException) {

            JOptionPane.showMessageDialog(null, "Data gagal disimpan!");
        }

        load_tabel_jurusan();

        reset();

    }//GEN-LAST:event_btnTambahJurusanActionPerformed

    private void btnResetJurusanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetJurusanActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_btnResetJurusanActionPerformed

    private void btnResetGuruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetGuruActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_btnResetGuruActionPerformed

    private void btnResetSiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetSiswaActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_btnResetSiswaActionPerformed

    private void btnResetKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetKelasActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_btnResetKelasActionPerformed

    private void tNomorHPSiswaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tNomorHPSiswaKeyTyped
        // TODO add your handling code here:
        char huruf = evt.getKeyChar();
        if (!Character.isDigit(huruf)) {
            evt.consume();
        }
    }//GEN-LAST:event_tNomorHPSiswaKeyTyped

    private void tNIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tNIPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tNIPActionPerformed

    private void tNIPKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tNIPKeyTyped
        // TODO add your handling code here:
        char huruf = evt.getKeyChar();
        if (!Character.isDigit(huruf)) {
            evt.consume();
        }
    }//GEN-LAST:event_tNIPKeyTyped

    private void tNISKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tNISKeyTyped
        // TODO add your handling code here:
        char huruf = evt.getKeyChar();
        if (!Character.isDigit(huruf)) {
            evt.consume();
        }

    }//GEN-LAST:event_tNISKeyTyped

    private void btnDasborActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDasborActionPerformed
        // TODO add your handling code here:
        pindahKartu("cardDasbor");
    }//GEN-LAST:event_btnDasborActionPerformed

    private void btnJurusanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJurusanActionPerformed
        // TODO add your handling code here:
        pindahKartu("cardJurusan");
    }//GEN-LAST:event_btnJurusanActionPerformed

    private void btnGuruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuruActionPerformed
        // TODO add your handling code here:
        pindahKartu("cardGuru");
    }//GEN-LAST:event_btnGuruActionPerformed

    private void btnKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKelasActionPerformed
        // TODO add your handling code here:
        pindahKartu("cardKelas");
    }//GEN-LAST:event_btnKelasActionPerformed

    private void btnSiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiswaActionPerformed
        // TODO add your handling code here:
        pindahKartu("cardSiswa");
    }//GEN-LAST:event_btnSiswaActionPerformed

    private void btnAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAboutActionPerformed
        // TODO add your handling code here:
        pindahKartu("cardAbout");
    }//GEN-LAST:event_btnAboutActionPerformed

    private void tblDataJurusanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataJurusanMouseClicked
        // TODO add your handling code here:
        int barisYangDipilih = tblDataJurusan.rowAtPoint(evt.getPoint());

        String kodeJurusan = tblDataJurusan.getValueAt(barisYangDipilih, 0).toString();

        String namaJurusan = tblDataJurusan.getValueAt(barisYangDipilih, 1).toString();

        tKodeJurusan.setText(kodeJurusan);

        tKodeJurusan.setEditable(false);

        tNamaJurusan.setText(namaJurusan);
    }//GEN-LAST:event_tblDataJurusanMouseClicked

    private void btnUbahJurusanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahJurusanActionPerformed
        // TODO add your handling code here:
        String kodeJurusan = tKodeJurusan.getText();

        String namaJurusan = tNamaJurusan.getText();

        String sql = "UPDATE jurusan SET nama_jur=? WHERE kode_jur=?";

        try {
            Connection con = koneksi.konek();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, namaJurusan);

            ps.setString(2, kodeJurusan);

            ps.execute();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (SQLException sQLException) {

            JOptionPane.showMessageDialog(null, "Data gagal diubah!");
        }

        load_tabel_jurusan();

        reset();
    }//GEN-LAST:event_btnUbahJurusanActionPerformed

    private void btnHapusJurusanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusJurusanActionPerformed
        // TODO add your handling code here:
        String kodeJurusan = tKodeJurusan.getText();

        String sql = "DELETE FROM jurusan WHERE kode_jur=?";
        //nah ini juga diuar
        try {
            Connection con = koneksi.konek();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, kodeJurusan);

            ps.execute();

            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        } catch (SQLException sQLException) {

            JOptionPane.showMessageDialog(null, "Data gagal dihapus!");
        }

        load_tabel_jurusan();

        reset();
    }//GEN-LAST:event_btnHapusJurusanActionPerformed

    private void tblDataGuruMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataGuruMouseClicked
        // TODO add your handling code here:
        int barisYangDipilih = tblDataGuru.rowAtPoint(evt.getPoint());

        String NIP = tblDataGuru.getValueAt(barisYangDipilih, 0).toString();

        String namaGuru = tblDataGuru.getValueAt(barisYangDipilih, 1).toString();

        String jenisKelaminGuru = tblDataGuru.getValueAt(barisYangDipilih, 2).toString();

        String alamatGuru = tblDataGuru.getValueAt(barisYangDipilih, 3).toString();

        tNIP.setText(NIP);

        tNIP.setEditable(false);

        tNamaGuru.setText(namaGuru);

        tAlamatGuru.setText(alamatGuru);

        switch (jenisKelaminGuru) {
            case "L":
                cJenisKelaminGuru.setSelectedItem("Laki-Laki");
                break;

            case "P":
                cJenisKelaminGuru.setSelectedItem("Perempuan");
                break;

            default:
                cJenisKelaminGuru.setSelectedItem(null);
                break;
        }
    }//GEN-LAST:event_tblDataGuruMouseClicked

    void comboJurusan() {
        try {
            String sql = "SELECT * FROM jurusan";

            Connection con = koneksi.konek();

            Statement statement = con.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                cJurusanKelas.addItem(resultSet.getString("nama_jur"));
            }
        } catch (SQLException sQLException) {

        }
        cJurusanKelas.setSelectedItem(null);
    }

    void comboWali() {
        try {
            String sql = "SELECT * FROM guru";

            Connection con = koneksi.konek();

            Statement statement = con.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                cWaliKelas.addItem(resultSet.getString("nama_guru"));
            }
        } catch (SQLException sQLException) {

        }
        cWaliKelas.setSelectedItem(null);
    }

    void comboKelas() {
        try {
            String sql = "SELECT * FROM kelas";

            Connection con = koneksi.konek();

            Statement statement = con.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                cWaliKelas.addItem(resultSet.getString("id_kelas"));
            }
        } catch (SQLException sQLException) {

        }
        cKelasSiswa.setSelectedItem(null);
    }

    String KodeJurusan(String NamaJurusan) {
        try {
            String sql = "SELECT * FROM jurusan WHERE nama_jur = ?";

            Connection con = koneksi.konek();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, NamaJurusan);

            ResultSet resultSet = ps.executeQuery(sql);

            while (resultSet.next()) {
                return resultSet.getString("kode_jur");
            }
        } catch (SQLException sQLException) {
            return "";

        }
        return "";
    }

    String NIP(String NamaGuru) {
        try {
            String sql = "SELECT * FROM guru WHERE nama_guru = ?";

            Connection con = koneksi.konek();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, NamaGuru);

            ResultSet resultSet = ps.executeQuery(sql);

            while (resultSet.next()) {
                return resultSet.getString("nip");
            }
        } catch (SQLException sQLException) {

        }
        return "";
    }

    private void cJenisKelaminGuruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cJenisKelaminGuruActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cJenisKelaminGuruActionPerformed

    private void btnTambahGuruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahGuruActionPerformed
        // TODO add your handling code here:
        String NIP = tNIP.getText();

        String namaGuru = tNamaGuru.getText();

        String jenisKelaminGuru = cJenisKelaminGuru.getSelectedItem().toString();

        String alamatGuru = tAlamatGuru.getText();

        String jkGuru = null;

        switch (jenisKelaminGuru) {
            case "Laki-Laki":
                jkGuru = "L";
                break;
            case "Perempuan":
                jkGuru = "P";
                break;
            default:
                jkGuru = null;
        }

        try {
            String sql = "INSERT INTO guru(nip, nama_guru, gender, alamat) VALUES (?,?,?,?)";

            Connection con = koneksi.konek();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, NIP);
            ps.setString(2, namaGuru);
            ps.setString(3, jkGuru);
            ps.setString(4, alamatGuru);
            ps.execute();

            JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");

        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Data gagal disimpan!");
        }

        load_tabel_guru();
        reset();
    }//GEN-LAST:event_btnTambahGuruActionPerformed

    private void btnUbahGuruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahGuruActionPerformed
        String NIP = tNIP.getText();

        String namaGuru = tNamaGuru.getText();

        String jenisKelaminGuru = cJenisKelaminGuru.getSelectedItem().toString();

        String alamatGuru = tAlamatGuru.getText();

        String jkGuru = null;

        String sql = "UPDATE guru SET nama_guru=?, gender=?, alamat=?, WHERE nip=?";

        try {
            Connection con = koneksi.konek();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, NIP);
            ps.setString(2, namaGuru);
            ps.setString(3, jkGuru);
            ps.setString(4, alamatGuru);
            ps.execute();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (SQLException sQLException) {

            JOptionPane.showMessageDialog(null, "Data gagal diubah!");
        }

        load_tabel_guru();

        reset();
    }//GEN-LAST:event_btnUbahGuruActionPerformed

    private void btnHapusGuruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusGuruActionPerformed
        // TODO add your handling code here:
        String NIP = tNIP.getText();

        String sql = "DELETE FROM guru WHERE nip=?";
        //ini kenapa di luar try?

        try {
            Connection con = koneksi.konek();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, NIP);
            ps.execute();

            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        } catch (SQLException sQLException) {

            JOptionPane.showMessageDialog(null, "Data gagal dihapus!");
        }
        load_tabel_guru();
        reset();
    }//GEN-LAST:event_btnHapusGuruActionPerformed

    private void tblDataKelasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataKelasMouseClicked
        // TODO add your handling code here:
        int barisYangDipilih = tblDataKelas.rowAtPoint(evt.getPoint());

        String KodeKelas = tblDataKelas.getValueAt(barisYangDipilih, 0).toString();
        String NamaKelas = tblDataKelas.getValueAt(barisYangDipilih, 1).toString();
        String Tingkatan = tblDataKelas.getValueAt(barisYangDipilih, 2).toString();
        String Jurusan = tblDataKelas.getValueAt(barisYangDipilih, 3).toString();
        String WaliKelas;

        if (tblDataKelas.getValueAt(barisYangDipilih, 4) != null) {
            WaliKelas = tblDataKelas.getValueAt(barisYangDipilih, 4).toString();
        } else {
            WaliKelas = null;
        }

        tKodeKelas.setText(KodeKelas);
        tKodeKelas.setEditable(false);

        tNamaKelas.setText(NamaKelas);

        cTingkatanKelas.setSelectedItem(Tingkatan);

        cJurusanKelas.setSelectedItem(Jurusan);

        cWaliKelas.setSelectedItem(WaliKelas);

    }//GEN-LAST:event_tblDataKelasMouseClicked

    private void btnTambahKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahKelasActionPerformed
        // TODO add your handling code here:
        String KodeKelas = tKodeKelas.getText();

        String NamaKelas = tNamaKelas.getText();

        String Tingkatan = cTingkatanKelas.getSelectedItem().toString();

        String Jurusan = KodeJurusan(cJurusanKelas.getSelectedItem().toString());

        String WaliKelas = NIP(cWaliKelas.getSelectedItem().toString());

        try {

            String sql = "INSERT INTO kelas(id_kelas, nama_kelas, tingkatan, kode_jur, nip_wali_kelas) VALUES (?,?,?,?,?)";

            Connection con = koneksi.konek();

            PreparedStatement statement = con.prepareStatement(sql);
            //apa bedanya ps dan statement?
            statement.setString(1, KodeKelas);
            statement.setString(2, NamaKelas);
            statement.setString(3, Tingkatan);
            statement.setString(4, Jurusan);
            statement.setString(5, WaliKelas);
            statement.execute();

            JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Data gagal disimpan!");
        }
        load_tabel_kelas();
        reset();
    }//GEN-LAST:event_btnTambahKelasActionPerformed

    private void btnUbahKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahKelasActionPerformed
        // TODO add your handling code here:
        String KodeKelas = tKodeKelas.getText();

        String NamaKelas = tNamaKelas.getText();

        String Tingkatan = cTingkatanKelas.getSelectedItem().toString();

        String Jurusan = KodeJurusan(cJurusanKelas.getSelectedItem().toString());

        String WaliKelas = NIP(cWaliKelas.getSelectedItem().toString());

        String sql = "UPDATE kelas SET nama_kelas=?, tingkatan=?, kode_jur=?, nip_wali_kelas=?, WHERE id_kelas=?";

        try {
            Connection con = koneksi.konek();

            PreparedStatement statement = con.prepareStatement(sql);

            //apa bedanya ps dan statement?
            statement.setString(1, KodeKelas);
            statement.setString(2, NamaKelas);
            statement.setString(3, Tingkatan);
            statement.setString(4, Jurusan);
            statement.setString(5, WaliKelas);
            statement.execute();

            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (SQLException sQLException) {

            JOptionPane.showMessageDialog(null, "Data gagal diubah!");
        }

        load_tabel_kelas();

        reset();
    }//GEN-LAST:event_btnUbahKelasActionPerformed

    private void btnHapusKelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusKelasActionPerformed
        // TODO add your handling code here:
        String KodeKelas = tKodeKelas.getText();

        try {
            String sql = "DELETE FROM kelas WHERE id_kelas=?";
            //sementara ini kok taruh di dalam try?
            Connection con = koneksi.konek();

            PreparedStatement statement = con.prepareStatement(sql);

            statement.setString(1, KodeKelas);
            statement.execute();

            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        } catch (SQLException sQLException) {

            JOptionPane.showMessageDialog(null, "Data gagal dihapus!");
        }
        load_tabel_kelas();
        reset();

    }//GEN-LAST:event_btnHapusKelasActionPerformed

    private void btnHapusSiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusSiswaActionPerformed
        // TODO add your handling code here:
        String nis = tNIS.getText();
        
        String sql = "DELETE FROM siswa WHERE nis=?";
        //ini juga di luar try, apa bedanya???
        
        try {
            Connection con = koneksi.konek();

            PreparedStatement statement = con.prepareStatement(sql);

            statement.setString(1, nis);
            statement.execute();

            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        } catch (SQLException sQLException) {

            JOptionPane.showMessageDialog(null, "Gagal Menghapus Data");
        }
        load_tabel_siswa();
        reset();
    }//GEN-LAST:event_btnHapusSiswaActionPerformed

    private void tblDataSiswaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataSiswaMouseClicked
        // TODO add your handling code here:
        // Mengambil indeks baris yang diklik pada tabel siswa
        int baris = tblDataSiswa.rowAtPoint(evt.getPoint());

// Mengambil nilai dari kolom pertama (NIS) pada baris yang diklik dan mengubah ke String
        String nis = tblDataSiswa.getValueAt(baris, 0).toString();

// Mengambil nilai dari kolom kedua (Nama Siswa)
        String namaSiswa = tblDataSiswa.getValueAt(baris, 1).toString();

// Mengambil objek dari kolom ketiga (Jenis Kelamin)
        Object jkObj = tblDataSiswa.getValueAt(baris, 2);

// Mengambil objek dari kolom keempat (Tempat Lahir)
        Object tempatObj = tblDataSiswa.getValueAt(baris, 3);

// Mengambil objek dari kolom kelima (Tanggal Lahir)
        Object tglObj = tblDataSiswa.getValueAt(baris, 4);

// Mengambil objek dari kolom keenam (Kelas)
        Object kelasObj = tblDataSiswa.getValueAt(baris, 5);

// Mengambil objek dari kolom ketujuh (Nomor HP)
        Object hpObj = tblDataSiswa.getValueAt(baris, 6);

// Menampilkan nilai NIS pada field input dan membuatnya tidak bisa diubah
        tNIS.setText(nis);
        tNIS.setEditable(false);

// Menampilkan nama siswa ke field input
        tNamaSiswa.setText(namaSiswa);

// Mengonversi objek menjadi string, jika null maka hasilnya null atau string kosong
        String jenisKelamin = (jkObj != null) ? jkObj.toString() : null;
        String tempatLahir = (tempatObj != null) ? tempatObj.toString() : "";
        String tglLahir = (tglObj != null) ? tglObj.toString() : null;
        String idKelas = (kelasObj != null) ? kelasObj.toString() : null;
        String noHP = (hpObj != null) ? hpObj.toString() : "";

// Menampilkan tempat lahir, no HP, dan memilih kelas sesuai data
        tTempatLahirSiswa.setText(tempatLahir);
        tNomorHPSiswa.setText(noHP);
        cKelasSiswa.setSelectedItem(idKelas);

// Jika tanggal lahir tidak null dan tidak kosong, ubah ke format Date dan tampilkan di komponen kalender
        if (tglLahir != null && !tglLahir.isEmpty()) {
            try {
                tTanggalLahirSiswa.setDate(java.sql.Date.valueOf(tglLahir));
            } catch (IllegalArgumentException e) {
                // Jika gagal parsing tanggal, kosongkan field tanggal
                tTanggalLahirSiswa.setDate(null);
            }
        } else {
            tTanggalLahirSiswa.setDate(null);
        }

// Konversi kode jenis kelamin ke bentuk tampilan yang dipahami pengguna
        switch (jenisKelamin) {
            case "L":
                cJenisKelaminSiswa.setSelectedItem("Laki - laki");
                break;
            case "P":
                cJenisKelaminSiswa.setSelectedItem("Perempuan");
                break;
            default:
                cJenisKelaminSiswa.setSelectedItem(null);
                break;
        }

        try {
            // Query untuk mengambil data alamat dan foto berdasarkan NIS
            String sql = "SELECT alamat, foto FROM siswa WHERE nis = ?";

            // Membuka koneksi ke database
            Connection conn = koneksi.konek();

            // Menyiapkan statement SQL dengan parameter
            PreparedStatement ps = conn.prepareStatement(sql);

            // Mengisi parameter dengan NIS
            ps.setString(1, nis);

            // Menjalankan query dan menyimpan hasilnya
            ResultSet rs = ps.executeQuery();

            // Jika data ditemukan
            if (rs.next()) {
                // Mengambil alamat dan foto dari hasil query
                String alamat = rs.getString("alamat");
                String foto = rs.getString("foto");

                // Menampilkan alamat ke field input
                tAlamatSiswa.setText(alamat);

                // Jika path foto tidak kosong, tampilkan gambar ke label foto
                if (foto != null && !foto.isEmpty()) {
                    ImageIcon icon = new ImageIcon(foto);
                    Image image = icon.getImage().getScaledInstance(tFotoSiswa.getWidth(), tFotoSiswa.getHeight(), Image.SCALE_SMOOTH);
                    tFotoPath.setText(foto);
                    tFotoSiswa.setText(null);
                    tFotoSiswa.setIcon(new ImageIcon(image));
                } else {
                    // Jika tidak ada foto, set teks "Foto" dan hapus icon
                    tFotoSiswa.setText("Foto");
                    tFotoSiswa.setIcon(null);
                }
            }
        } catch (SQLException e) {
            // Menampilkan error ke konsol jika terjadi kesalahan SQL
            System.err.println(e.getMessage());
        }
    }//GEN-LAST:event_tblDataSiswaMouseClicked

    private void tFotoSiswaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tFotoSiswaMouseClicked
        // TODO add your handling code here:
        try {
            JFileChooser chooser = new JFileChooser();

            int result = chooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {

                File file = chooser.getSelectedFile();

                if (file != null) {

                    ImageIcon icon = new ImageIcon(file.toString());

                    Image image = icon.getImage().getScaledInstance(
                            tFotoSiswa.getWidth(),
                            tFotoSiswa.getHeight(),
                            Image.SCALE_SMOOTH
                    );

                    ImageIcon ic = new ImageIcon(image);

                    tFotoSiswa.setText(null);

                    tFotoSiswa.setIcon(ic);

                    String filename = file.getAbsolutePath();
                    tFotoPath.setText(filename);
                }
            } else {
                System.out.println("Pemilihan file dibatalkan oleh pengguna.");
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Error Upload: " + e.getMessage());
        }
    }//GEN-LAST:event_tFotoSiswaMouseClicked

    private void btnTambahSiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahSiswaActionPerformed
        // TODO add your handling code here:
        // Mengambil teks dari field NIS
        String nis = tNIS.getText();

// Mengambil teks dari field Nama Siswa
        String namaSiswa = tNamaSiswa.getText();

// Mengambil item yang dipilih dari combo box jenis kelamin dan mengubahnya menjadi string
        String jenisKelamin = cJenisKelaminSiswa.getSelectedItem().toString();

// Variabel untuk menyimpan hasil konversi jenis kelamin (L/P)
        String jK = null;

// Mengambil teks dari field Tempat Lahir
        String tempatLahir = tTempatLahirSiswa.getText();

// Mengambil tanggal dari komponen kalender
        Date tglLahirDate = tTanggalLahirSiswa.getDate();

// Mengubah tanggal lahir menjadi format "yyyy-MM-dd"
        String tglLahir = new SimpleDateFormat("yyyy-MM-dd").format(tglLahirDate);

// Mengambil teks dari field nomor HP
        String hp = tNomorHPSiswa.getText();

// Mengambil item yang dipilih dari combo box kelas
        String kelas = cKelasSiswa.getSelectedItem().toString();

// Mengambil teks dari field alamat
        String alamat = tAlamatSiswa.getText();

// Mengambil path file dari label path foto
        String filePath = tFotoPath.getText();

// Konversi jenis kelamin dari teks menjadi kode (L atau P)
        switch (jenisKelamin) {
            case "Laki - laki":
                jK = "L";
                break;
            case "Perempuan":
                jK = "P";
                break;
            default:
                jK = null;
                break;
        }

// Variabel untuk menyimpan path file foto tujuan
        String foto = null;

// Mengecek apakah ada path file foto yang dipilih
        if (filePath.length() != 0) {
            try {
                // Menyimpan path sumber file
                String sourcePath = filePath;
                File sourceFile = new File(sourcePath);

                // Menentukan folder tujuan untuk menyimpan foto
                String destinationFolderPath = "src/foto/";
                File destinationFolder = new File(destinationFolderPath);

                // Jika folder tujuan belum ada, buat folder tersebut
                if (!destinationFolder.exists()) {
                    destinationFolder.mkdir();
                }

                // Mengambil ekstensi file (contoh: jpg, png, dll)
                String extension = sourcePath.substring(sourcePath.lastIndexOf('.') + 1);

                // Membuat nama file baru yang unik berdasarkan timestamp
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String destinationFileName = "foto-" + timestamp + "." + extension;

                // Membuat file tujuan dengan path dan nama file baru
                File destinationFile = new File(destinationFolderPath + destinationFileName);

                // Menyalin file dari sumber ke tujuan
                Files.copy(sourceFile.toPath(), destinationFile.toPath());

                // Menyimpan path foto yang telah dipindahkan
                foto = destinationFile.getPath();

            } catch (Exception e) {
                // Menampilkan pesan error jika gagal mengupload file
                JOptionPane.showMessageDialog(null, "Gagal upload file: " + e.getMessage());
            }
        } else {
            // Jika tidak ada file yang dipilih, set null
            foto = null;
        }

        try {
            // Query SQL untuk menyimpan data siswa ke database
            String sql = "INSERT INTO siswa(nis,nama_siswa,gender,tempat_lahir,tgl_lahir,alamat,no_hp,id_kelas,foto)"
                    + " VALUES(?,?,?,?,?,?,?,?,?)";

            // Membuka koneksi ke database
            Connection conn = koneksi.konek();

            // Menyiapkan statement SQL dengan parameter
            PreparedStatement statement = conn.prepareStatement(sql);

            // Mengisi nilai parameter satu per satu
            statement.setString(1, nis);
            statement.setString(2, namaSiswa);
            statement.setString(3, jK);
            statement.setString(4, tempatLahir);
            statement.setString(5, tglLahir);
            statement.setString(6, alamat);
            statement.setString(7, hp);
            statement.setString(8, kelas);
            statement.setString(9, foto);

            // Menjalankan query penyimpanan
            statement.execute();

            // Menampilkan pesan bahwa data berhasil disimpan
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
        } catch (SQLException e) {
            // Menampilkan pesan jika terjadi kesalahan saat menyimpan data
            JOptionPane.showMessageDialog(null, "Data gagal disimpan!");
        }

// Memuat ulang data siswa ke tabel
        load_tabel_siswa();

// Mengosongkan semua input form setelah data disimpan
        reset();
    }//GEN-LAST:event_btnTambahSiswaActionPerformed

    private void btnUbahSiswaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahSiswaActionPerformed
        // TODO add your handling code here:
        // Mengambil NIS dari field input
        String nis = tNIS.getText();

// Mengambil Nama Siswa dari field input
        String namaSiswa = tNamaSiswa.getText();

// Mengambil nilai dari combo box jenis kelamin dan mengubah menjadi String
        String jenisKelamin = cJenisKelaminSiswa.getSelectedItem().toString();

// Variabel untuk menyimpan kode jenis kelamin ('L' atau 'P')
        String jK = null;

// Mengambil Tempat Lahir dari field input
        String tempatLahir = tTempatLahirSiswa.getText();

// Mengambil tanggal lahir dari komponen kalender
        Date tglLahirDate = tTanggalLahirSiswa.getDate();

// Mengubah tanggal lahir menjadi format "yyyy-MM-dd"
        String tglLahir = new SimpleDateFormat("yyyy-MM-dd").format(tglLahirDate);

// Mengambil Nomor HP dari field input
        String hp = tNomorHPSiswa.getText();

// Mengambil Kelas dari combo box
        String kelas = cKelasSiswa.getSelectedItem().toString();

// Mengambil Alamat dari field input
        String alamat = tAlamatSiswa.getText();

// Mengambil path file foto dari field input tersembunyi
        String filePath = tFotoPath.getText();

// Mengonversi pilihan jenis kelamin ke kode (L/P)
        switch (jenisKelamin) {
            case "Laki - laki":
                jK = "L";
                break;
            case "Perempuan":
                jK = "P";
                break;
            default:
                jK = null;
                break;
        }

// Variabel untuk menyimpan path foto asli yang tersimpan di database
        String fotoAsli = null;

        try {
            // Query untuk mengambil path foto berdasarkan NIS
            String sql = "SELECT foto FROM siswa WHERE nis = ?";
            Connection conn = koneksi.konek();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nis);
            ResultSet rs = ps.executeQuery();

            // Jika data ditemukan, simpan path foto ke variabel fotoAsli
            if (rs.next()) {
                fotoAsli = rs.getString("foto");
            }
        } catch (SQLException e) {
            // Tampilkan pesan jika gagal mengambil foto dari database
            JOptionPane.showMessageDialog(null, "Gagal mengambil foto asli: " + e.getMessage());
        }

// Menentukan apakah foto diubah oleh pengguna
        boolean fotoDiubah = (fotoAsli == null && !filePath.isEmpty())
                || (fotoAsli != null && !fotoAsli.equals(filePath));

// Jika foto diubah, variabel 'foto' akan diisi dengan path baru
        String foto = fotoAsli;

        if (fotoDiubah) {
            try {
                // Ambil file dari path baru
                File sourceFile = new File(filePath);

                // Dapatkan ekstensi file
                String extension = filePath.substring(filePath.lastIndexOf('.') + 1);

                // Buat nama file baru berdasarkan waktu agar unik
                String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String destinationPath = "src/foto/foto-" + timestamp + "." + extension;

                // Salin file ke folder tujuan
                File destFile = new File(destinationPath);
                Files.copy(sourceFile.toPath(), destFile.toPath());

                // Simpan path tujuan ke variabel 'foto'
                foto = destinationPath;

            } catch (Exception e) {
                // Tampilkan pesan jika gagal upload file
                JOptionPane.showMessageDialog(null, "Gagal upload file: " + e.getMessage());
            }
        }

        try {
            // Query SQL berbeda tergantung apakah foto diubah atau tidak
            String sql2;
            if (fotoDiubah) {
                sql2 = "UPDATE siswa SET nama_siswa=?, gender=?, tempat_lahir=?, "
                        + "tgl_lahir=?, alamat=?, no_hp=?, id_kelas=?, foto=? WHERE nis=?";
            } else {
                sql2 = "UPDATE siswa SET nama_siswa=?, gender=?, tempat_lahir=?, "
                        + "tgl_lahir=?, alamat=?, no_hp=?, id_kelas=? WHERE nis=?";
            }

            // Membuka koneksi ke database
            Connection conn = koneksi.konek();

            // Menyiapkan statement untuk eksekusi SQL
            PreparedStatement statement = conn.prepareStatement(sql2);

            // Menetapkan parameter umum
            statement.setString(1, namaSiswa);
            statement.setString(2, jK);
            statement.setString(3, tempatLahir);
            statement.setString(4, tglLahir);
            statement.setString(5, alamat);
            statement.setString(6, hp);
            statement.setString(7, kelas);

            // Jika foto diubah, tetapkan parameter tambahan
            if (fotoDiubah) {
                statement.setString(8, foto);
                statement.setString(9, nis);
            } else {
                statement.setString(8, nis);
            }

            // Eksekusi perintah update
            statement.execute();

            // Tampilkan pesan sukses
            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");

        } catch (SQLException e) {
            // Tampilkan pesan jika update gagal
            JOptionPane.showMessageDialog(null, "Gagal memperbarui data: " + e.getMessage());
        }

// Muat ulang tabel agar perubahan terlihat
        load_tabel_siswa();

// Kosongkan form setelah proses selesai
        reset();

    }//GEN-LAST:event_btnUbahSiswaActionPerformed

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
            UIManager.put("Button.arc", 15);
            UIManager.put("TextComponent.arc", 15);
            UIManager.put("Button.borderWidth", 0);
            UIManager.put("Component.borderWidth", 0);
            UIManager.put("Component.focusWidth", 0);
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AngkaJurusan;
    private javax.swing.JPanel Guru;
    private javax.swing.JPanel Jurusan;
    private javax.swing.JPanel Kelas;
    private javax.swing.JPanel Siswa;
    private javax.swing.JPanel angkaGuru;
    private javax.swing.JPanel angkaKelas;
    private javax.swing.JPanel angkaSiswa;
    private javax.swing.JPanel atasAboutUs;
    private javax.swing.JPanel atasGuru;
    private javax.swing.JPanel atasJurusan;
    private javax.swing.JPanel atasKelas;
    private javax.swing.JPanel atasSiswa;
    private javax.swing.JPanel bawah;
    private javax.swing.JPanel bawahJurusan;
    private javax.swing.JPanel bawahJurusan1;
    private javax.swing.JButton btnAbout;
    private javax.swing.JButton btnDasbor;
    private javax.swing.JButton btnGuru;
    private javax.swing.JButton btnHapusGuru;
    private javax.swing.JButton btnHapusJurusan;
    private javax.swing.JButton btnHapusKelas;
    private javax.swing.JButton btnHapusSiswa;
    private javax.swing.JButton btnJurusan;
    private javax.swing.JButton btnKelas;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnResetGuru;
    private javax.swing.JButton btnResetJurusan;
    private javax.swing.JButton btnResetKelas;
    private javax.swing.JButton btnResetSiswa;
    private javax.swing.JButton btnSiswa;
    private javax.swing.JButton btnTambahGuru;
    private javax.swing.JButton btnTambahJurusan;
    private javax.swing.JButton btnTambahKelas;
    private javax.swing.JButton btnTambahSiswa;
    private javax.swing.JButton btnUbahGuru;
    private javax.swing.JButton btnUbahJurusan;
    private javax.swing.JButton btnUbahKelas;
    private javax.swing.JButton btnUbahSiswa;
    private javax.swing.JComboBox<String> cJenisKelaminGuru;
    private javax.swing.JComboBox<String> cJenisKelaminSiswa;
    private javax.swing.JComboBox<String> cJurusanKelas;
    private javax.swing.JComboBox<String> cKelasSiswa;
    private javax.swing.JComboBox<String> cTingkatanKelas;
    private javax.swing.JComboBox<String> cWaliKelas;
    private javax.swing.JPanel cardAbout;
    private javax.swing.JPanel cardDasbor;
    private javax.swing.JPanel cardGuru;
    private javax.swing.JPanel cardJurusan;
    private javax.swing.JPanel cardKelas;
    private javax.swing.JPanel cardSiswa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel kontenGuru;
    private javax.swing.JPanel kontenKelas;
    private javax.swing.JPanel kontenSiswa;
    private javax.swing.JPanel pnlBawahSiswa;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlFotoSiswa;
    private javax.swing.JPanel pnlInput;
    private javax.swing.JPanel pnlInputKelas;
    private javax.swing.JPanel pnlInputSiswa1;
    private javax.swing.JPanel pnlInputSiswa2;
    private javax.swing.JPanel pnlOutput;
    private javax.swing.JPanel pnlOutputKelas;
    private javax.swing.JPanel pnlOutputSiswa;
    private javax.swing.JPanel pnlSideBar;
    private javax.swing.JScrollPane scrAlamatSiswa;
    private javax.swing.JScrollPane scrDataSiswa;
    private javax.swing.JScrollPane scrlDataGuru;
    private javax.swing.JScrollPane scrlDataJurusan;
    private javax.swing.JScrollPane scrlDataKelas;
    private javax.swing.JTextArea tAlamatGuru;
    private javax.swing.JTextArea tAlamatSiswa;
    private javax.swing.JLabel tFotoPath;
    private javax.swing.JLabel tFotoSiswa;
    private javax.swing.JLabel tJumlahGuru;
    private javax.swing.JLabel tJumlahJurusan;
    private javax.swing.JLabel tJumlahKelas;
    private javax.swing.JLabel tJumlahSiswa;
    private javax.swing.JTextField tKodeJurusan;
    private javax.swing.JTextField tKodeKelas;
    private javax.swing.JTextField tNIP;
    private javax.swing.JTextField tNIS;
    private javax.swing.JTextField tNamaGuru;
    private javax.swing.JTextField tNamaJurusan;
    private javax.swing.JTextField tNamaKelas;
    private javax.swing.JTextField tNamaSiswa;
    private javax.swing.JTextField tNomorHPSiswa;
    private com.toedter.calendar.JDateChooser tTanggalLahirSiswa;
    private javax.swing.JTextField tTempatLahirSiswa;
    private javax.swing.JLabel tTittle;
    private javax.swing.JLabel tTittle1;
    private javax.swing.JLabel tTittle2;
    private javax.swing.JLabel tTittle3;
    private javax.swing.JTable tblDataGuru;
    private javax.swing.JTable tblDataJurusan;
    private javax.swing.JTable tblDataKelas;
    private javax.swing.JTable tblDataSiswa;
    private javax.swing.JPanel titleGuru;
    private javax.swing.JPanel titleJurusan;
    private javax.swing.JPanel titleKelas;
    private javax.swing.JPanel tittleSiswa;
    private javax.swing.JPanel tombolGuru;
    private javax.swing.JPanel tombolKelas;
    private javax.swing.JPanel tombolSiswa;
    // End of variables declaration//GEN-END:variables
}
