		<!-- Header -->
				<%@include file="header.jsp" %>
		<!-- /.Header -->

		<!-- Main Sidebar Container -->
			<%@include file="sidebar.jsp" %>
		<!-- /.sidebar -->

		 <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>View Invoice</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="index">Home</a></li>
              <li class="breadcrumb-item"><a href="invoice">Invoice</a></li>
              <li class="breadcrumb-item active">View</li>
            </ol>
          </div>
        </div>
   
      </div><!-- /.container-fluid -->
    </section>

   <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-12">
       
            <!-- Main content -->
            <div class="invoice p-3 mb-3">
              <!-- title row -->
              <div class="row">
                <div class="col-12">
                  <h4>
                    <i class="fas fa-globe"></i> ${name}
                    <small class="float-right">Date: ${paymentDue}</small>
                  </h4>
                </div>
                <!-- /.col -->
              </div>
              <!-- info row -->
              <div class="row invoice-info">
                <div class="col-sm-4 invoice-col">
                  From
                  <address>
                    <strong>${name}</strong><br>
                    ${address}<br>
                    Phone: ${msisdn}<br>
                    Email: ${email}
                  </address>
                </div>
                <!-- /.col -->
                <div class="col-sm-4 invoice-col">
                  To
                  <address>
                    <strong>${memberName}</strong><br>
                    ${memberAddress}<br>
                    Phone: ${memberMsisdn}<br>
                    Email: ${memberEmail}
                  </address>
                </div>
                <!-- /.col -->
                <div class="col-sm-4 invoice-col">
                  <b>Invoice #${invoiceNo}</b><br>
                  <br>
                  <b>Billing ID:</b> ${billingID}<br>
                  <b>Payment Code:</b> ${paymentCode}<br>
                  <b>Payment Due:</b> ${paymentDue}
                </div>
                <!-- /.col -->
                
              </div>
              <!-- /.row -->

              <!-- Table row -->
              <div class="row">
                <div class="col-12 table-responsive">
                  <table class="table table-striped">
                    <thead>
                    <tr>
                    	 <th>No</th>
                      <th>Item</th>
                      <th>Amount</th>
                    </tr>
                    </thead>
                    <tbody>
                    ${table}
                    </tbody>
                  </table>
                </div>
                <!-- /.col -->
              </div>
              <!-- /.row -->

              <div class="row">
                <!-- accepted payments column -->
                <div class="col-6">
                  <p class="lead">Payment Methods:</p>
                  <img src="dist/img/credit/visa.png" alt="Visa">
                  <img src="dist/img/credit/mastercard.png" alt="Mastercard">
          
                  <p class="text-muted well well-sm shadow-none" style="margin-top: 10px;">
                    You can pay directly using QRIS by scanning the QR Code below.
                  </p>
                  <img src="dist/img/qr.png"/> <!-- img src="dist/img/paid-stamp.jpg" width="220" height="120" / -->
     
                  <div>&nbsp;</div>
                </div>
                <!-- /.col -->
                <div class="col-6">
                  <p class="lead">Amount Due ${paymentDue}</p>
			      <div class="table-responsive">
                    <table class="table">
                      <tr>
                        <th style="width:50%">Subtotal:</th>
                        <td>Rp. ${amount},-</td>
                      </tr>
                      <tr>
                        <th>Total Fee</th>
                        <td>Rp. 0,-</td>
                      </tr>
                      <tr>
                        <th>Total Discount</th>
                        <td>Rp. 0,-</td>
                      </tr>
                      <tr>
                        <th>Total:</th>
                        <td>Rp. ${amount},-</td>
                      </tr>
                    </table>
                  </div>
                </div>
                <!-- /.col -->
              </div>
              <!-- /.row -->

              <!-- this row will not appear when printing -->
              <div class="row no-print">
                <div class="col-12">
                  <a href="invoicePrint?invoiceID=${invoiceID}" target="_blank" class="btn btn-default"><i class="fas fa-print"></i> Print</a>
                </div>
              </div>
            </div>
            <!-- /.invoice -->
          </div><!-- /.col -->
        </div><!-- /.row -->
      </div><!-- /.container-fluid -->
    </section>
    <!-- /.content -->
    
  </div>
  <!-- /.content-wrapper -->

		<!-- footer -->
			<%@include file="footer.jsp" %>
		<!-- /.footer -->
		
</body>
</html>
