import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class PPBO_12_L0123021 {

    // memeriksa apakah sebuah bilangan adalah bilangan prima
    public static boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }

    // (single-thread)
    public static int primeSingleThread(int start, int end) {
        int count = 0;
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) {
                count++;
            }
        }
        return count;
    }

    // (multithreading)
    public static int primeMultiThread(int start, int end) throws InterruptedException {
        AtomicInteger count = new AtomicInteger(0);
        Thread[] threads = new Thread[5];
        int chunkSize = (end - start + 1) / 5;
        int remainder = (end - start + 1) % 5;

        int a = start;
        int b = start + chunkSize - 1;

        for (int i = 0; i < 5; i++) {
            if (i == 4) {
                b += remainder; // menambahkan sisa ke bagian terakhir
            }
            // menyimpan nilai awal dan akhir dari rentang yang akan diproses oleh thread saat ini
            final int startRange = a;
            final int endRange = b;

            threads[i] = new Thread(() -> {
                int partialCount = primeSingleThread(startRange, endRange);
                count.addAndGet(partialCount);
            });

            threads[i].start();

            a = b + 1;
            b = a + chunkSize - 1;
        }

        for (Thread thread : threads) {
            thread.join(); // menunggu semua thread selesai
        }

        return count.get();
    }
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        boolean repeat = true;

        while (repeat) {
            // menu
            System.out.println("\n\t\t! Selamat Datang di Program Penghitung Bilangan Prima !");
            System.out.println("\t\t\t\t by: ICAK L0123021");
            System.out.println("\nMasukkan rentang bilangan:");
            System.out.print("Bilangan Awal: ");
            int startRange = scanner.nextInt();
            System.out.print("Bilangan Akhir: ");
            int endRange = scanner.nextInt();

            long startTime = System.nanoTime();

            // pilihan pendekatan
            System.out.println("Silahkan memilih pendekatan penghitungan:");
            System.out.println("1. Single Thread");
            System.out.println("2. Multi Thread");
            System.out.print("Pilihan Anda (1/2): ");
            int choice = scanner.nextInt();

            int count = 0;
            switch (choice) {
                case 1 -> count = primeSingleThread(startRange, endRange);
                case 2 -> count = primeMultiThread(startRange, endRange);
                default -> {
                    System.out.println("Maaf pilihan yang dimasukkan tidak valid.");
                    continue; // kembali ke awal loop jika pilihan tidak valid
                }
            }

            long endTime = System.nanoTime();

            // menampilkan hasil dengan rentang yang diinput
            System.out.println("\nJumlah bilangan prima dalam rentang " + startRange + " hingga " + endRange + ": " + count);
            System.out.println("Waktu eksekusi (dalam nanosecond): " + (endTime - startTime));
            System.out.println("\nTerima Kasih Telah Menggunakan Program Ini!");

            // jika user ingin mengulang
            System.out.print("\nApa ingin melakukan perhitungan lagi? (ya/tidak): ");
            String response = scanner.next();
            repeat = response.equalsIgnoreCase("ya");
        }

        // Menutup scanner
        scanner.close();
    }
}

/*Hasil Perbandingan dan Temuan:
 
1. Waktu Eksekusi: pendekatan multithreading lebih cepat dibandingkan 
 dengan pendekatan single-thread, terutama untuk rentang yang lebih besar. 
 Karena pembagian beban kerja di antara beberapa thread; pemrosesan secara paralel.
  
 2. Efisiensi: di sisi lain multithreading dapat memanfaatkan CPU multi-core dengan lebih baik, 
  sehingga meningkatkan efisiensi proses.Namun, untuk rentang yang sangat kecil, 
  overhead dari pembuatan thread akan membuat multithreading lebih lambat dibandingkan single-thread.
 
3. Kompleksitas: Single-Thread: O(m * √n)
                 Multi-Thread: O(√n)

4. Scalability: multithreading lebih mudah untuk diperluas dengan kata lain 
  masih dapat menambah jumlah thread untuk menangani rentang yang lebih besar 
  dan menjadikan program lebih fleksibel.
 
Kesimpulan:
 menggunakan multithreading dalam program ini memberikan keuntungan dalam hal kecepatan dan efisiensi, 
 terutama untuk rentang bilangan yang besar. 
 Namun, juga perlu mempertimbangkan kompleksitas tambahan yang datang dengan pengelolaan beberapa thread. 
 Jika program hanya bekerja dengan rentang kecil, pendekatan single-thread mungkin sudah cukup dan lebih sederhana.
 */ 