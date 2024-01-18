package com.example.hostel;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

public class FeeDetailsA {
    private List<Receipt> receipts;

    public FeeDetailsA() {
        // Default constructor required for calls to DataSnapshot.getValue(FeeDetailsA.class)
    }

    public List<Receipt> getReceipts() {
        return receipts;
    }

    public void addFeeReceipt(Receipt receipt) {
        if (receipts == null) {
            receipts = new ArrayList<>();
        }
        receipts.add(receipt);
    }

    public static class Receipt {
        private String pdfUrl;
        private String pdfName;
        private String studentName;
        public Receipt() {}

        public Receipt(String pdfUrl, String pdfName, String studentName) {
            this.pdfUrl = pdfUrl;
            this.pdfName = pdfName;
            this.studentName = studentName;
        }

        public String getPdfUrl() {
            return pdfUrl;
        }

        public String getPdfName() {
            return pdfName;
        }

        public String getStudentName() {
            return studentName;
        }
    }
}
