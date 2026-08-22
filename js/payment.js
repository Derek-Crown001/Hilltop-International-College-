/**
 * Hilltop International College Portal - Paystack & Flutterwave Payment Simulator
 * Supports Card, Bank Transfer, and USSD with automated instant receipt reconciliation.
 */

window.PaymentEngine = {
    currentInvoice: null,
    selectedChannel: 'card',

    openCheckout: function(invoiceId) {
        const inv = HilltopData.feeInvoices.find(i => i.id === invoiceId);
        if (!inv) return;

        this.currentInvoice = inv;
        this.selectedChannel = 'card';

        const amountToPay = inv.balanceDue > 0 ? inv.balanceDue : inv.totalAmount;
        document.getElementById('payModalAmount').innerText = `₦${amountToPay.toLocaleString('en-NG')}.00`;
        document.getElementById('payModalTitle').innerText = inv.title;

        this.switchChannel('card');
        document.getElementById('paymentModal').classList.add('active');
    },

    closeCheckout: function() {
        document.getElementById('paymentModal').classList.remove('active');
        this.currentInvoice = null;
    },

    switchChannel: function(channel) {
        this.selectedChannel = channel;
        const tabs = document.querySelectorAll('.channel-tab');
        tabs.forEach(t => t.classList.remove('active'));

        const contents = document.querySelectorAll('.channel-content');
        contents.forEach(c => c.classList.add('d-none'));

        if (channel === 'card') {
            tabs[0].classList.add('active');
            document.getElementById('channelCard').classList.remove('d-none');
        } else if (channel === 'transfer') {
            tabs[1].classList.add('active');
            document.getElementById('channelTransfer').classList.remove('d-none');
        } else if (channel === 'ussd') {
            tabs[2].classList.add('active');
            document.getElementById('channelUssd').classList.remove('d-none');
        }
    },

    executePayment: function() {
        if (!this.currentInvoice) return;

        const inv = this.currentInvoice;
        const paidAmount = inv.balanceDue > 0 ? inv.balanceDue : inv.totalAmount;
        const ref = 'PSTK_REF_' + Math.floor(1000000000 + Math.random() * 9000000000);
        const rcpNo = 'HIC-RCP-2026-0' + Math.floor(100 + Math.random() * 900);

        // Update Invoice status
        inv.amountPaid += paidAmount;
        inv.balanceDue = Math.max(0, inv.totalAmount - inv.amountPaid);
        inv.status = inv.balanceDue === 0 ? 'PAID' : 'PARTIAL';

        // Find Student
        const student = HilltopData.students.find(s => s.id === inv.studentId);
        const studentName = student ? student.name : "Chinedu Emmanuel Okafor";
        const admissionNo = student ? student.admissionNo : "HIC/2023/0482";

        // Create official payment receipt
        const newReceipt = {
            receiptNumber: rcpNo,
            transactionRef: ref,
            studentName: studentName,
            admissionNo: admissionNo,
            payerName: "Barr. Samuel Okafor",
            amountPaid: paidAmount,
            paymentGateway: `Paystack (${this.selectedChannel.toUpperCase()})`,
            paymentDate: new Date().toLocaleDateString('en-NG', { month: 'short', day: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' }),
            termSession: `${HilltopData.term} ${HilltopData.session}`
        };

        HilltopData.paymentReceipts.unshift(newReceipt);

        this.closeCheckout();
        showToast(`Payment of ₦${paidAmount.toLocaleString('en-NG')} successful! Receipt #${rcpNo} issued.`);

        // Refresh current portal view
        if (HilltopData.currentRole === 'parent') {
            renderParentPortal();
        } else if (HilltopData.currentRole === 'student') {
            renderStudentPortal();
        }
    }
};

window.switchPayChannel = function(channel) {
    PaymentEngine.switchChannel(channel);
};

window.executePayment = function() {
    PaymentEngine.executePayment();
};
