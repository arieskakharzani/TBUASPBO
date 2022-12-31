import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

//class program, running di class program
public class Program {
    // koneksi database
    // static String jdbc_driver = "com.mysql.jdbc.Driver";
    static Connection conn;
    static String link = "jdbc:mysql://localhost:3306/pinjam_buku";
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        // date
        DateFormat formatTanggal = new SimpleDateFormat("dd MMMM yyyy");
        DateFormat formatJam = new SimpleDateFormat("HH:mm:ss");
        Date tanggal = new Date();

        String salamSapa = "Selamat Pagi, Semoga harimu baik";
        String sapa = salamSapa.replace("Selamat Pagi", "\nHello");
        // method string
        System.out.println(sapa.toLowerCase());
        System.out.println("\nSelamat datang di program peminjaman buku");
        menu();
        // date, print tanggal pada terminal
        System.out.println("====================================");
        System.out.println("=Dibuat pada     : " + formatTanggal.format(tanggal) + "=");
        System.out.println("=Diupdate pada   : " + formatJam.format(tanggal) + " WIB    =");
        System.out.println("====================================");
    }

    // untuk menyimpan beberapa menu pada terminal
    private static void menu() throws SQLException {
        boolean balikMenu = true;
        boolean balikTanya = true;
        Integer pilihan;
        String pertanyaan;// utk menyimpan inputan dri user apakah ingin masuk ke program utama atau tidak

        // perulangan while
        while (balikMenu)// scr otomatis balikMenu = true, maka dijalankan blok kodingan berikut
        {
            System.out.println("\n=============== M E N U ================");
            System.out.println("    1.\tLihat Data Buku");
            System.out.println("    2.\tUbah Data Buku");
            System.out.println("    3.\tPinjam buku");
            System.out.println("    4.\tBalikin buku");
            System.out.println("    5.\tExit");
            System.out.print("\tPilihan Anda (1/2/3/4/5): ");

            pilihan = input.nextInt();// inputan user
            input.nextLine();
            Buku buku = new Buku(1);
            PinjamBuku pinjambuku = new PinjamBuku();
            BalikinBuku balikinbuku = new BalikinBuku();
            // percabangan switch case
            switch (pilihan) {
                case 1:
                    buku.view();
                    balikTanya = true;
                    break;

                case 2:
                    admin();
                    buku.update();
                    balikTanya = true;
                    break;

                case 3:
                    pinjambuku.save();
                    balikTanya = true;
                    break;

                case 4:
                    balikinbuku.delete();
                    balikTanya = true;
                    break;

                case 5:
                    balikTanya = false;
                    balikMenu = false;
                    break;

                default:
                    System.out.println("Mohon inputkan angka yang tersedia pada Menu!");
                    break;
            }// jika user melakukan input selain integer, maka akan terjadi error dg
             // exception input mismatch

            // perulangan while
            while (balikTanya)// scr otomatis balikTanya = true, maka dijalankan blok kodingan berikut
            {
                System.out.print("Apakah ingin kembali ke menu utama [y/n]? ");
                pertanyaan = input.nextLine();
                // percabangan if else if
                if (pertanyaan.equalsIgnoreCase("n")) // method string utk mengabaikan jenis huruf kapital ataupun kecil
                                                      // yang diinputkan
                {
                    balikMenu = false;
                    balikTanya = false;
                } else if (pertanyaan.equalsIgnoreCase("y")) // method string
                {
                    balikMenu = true;
                    balikTanya = false;
                } else {
                    System.out.println("Mohon inputkan 'y' atau 'n' saja");
                }
            }
        }
        System.out.println("\n\n\t\tSelesai\n");
    }

    // menggunakan collection HashMap
    // void admin() utk memanggil nilai yg ada di database, yaitu username dan
    // password
    // yg digunakan utk melakukan perubahan data pada data buku
    private static void admin() throws SQLException {
        // membuat objek HashMap baru
        Map<String, String> Login = new HashMap<String, String>();// key dan value

        // mengakses data di database buku pada tabel admin
        String inputUsername, inputPassword;
        String sql = "SELECT * FROM admin";
        boolean relogin = true;

        conn = DriverManager.getConnection(link, "root", "");
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(sql);

        // perulangan while
        while (result.next()) {
            // mengambil nilai di database dan menyimpannya ke dalam variable
            String username = result.getString("username");
            String password = result.getString("password");

            // input key dan value
            Login.put(username, password);
        }
        System.out.println("Silakan login terlebih dahulu");

        // perulangan while utk melakukan relogin
        while (relogin) {
            System.out.println("Masukkan username dan password yang benar!");
            System.out.print("Username : ");
            inputUsername = input.nextLine();
            System.out.print("Password : ");
            inputPassword = input.nextLine(); // inputan dari user

            // percabangan if utk pengecekan
            if (Login.containsValue(inputUsername) == true) // method bawaan HashMap
            {
                System.out.println("test");
                // percabangan if
                if (Login.get(inputUsername).equals(inputPassword)) // method bawaan HashMap dan method string
                {
                    System.out.println("Berhasil login");
                    relogin = false;// jika relogin bernilai false maka berhasil login
                } else {
                    relogin = true;// maka akan dilakukan relogin terus sampai user memasukkan data yang benar
                }
            } else {
                // System.out.println("Login Berhasil");
                relogin = true;
            }
        }
        statement.close();
    }

}
