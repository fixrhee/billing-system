		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper">
			<!-- Content Header (Page header) -->
			<div class="content-header">
				<div class="container-fluid">
					<div class="row mb-2">
						<div class="col-sm-6">
							<h1 class="m-0 text-dark">Dashboard</h1>
						</div>
						<!-- /.col -->
						<div class="col-sm-6">
							<ol class="breadcrumb float-sm-right">
								<li class="breadcrumb-item"><a href="#">Home</a></li>
								<li class="breadcrumb-item active">Dashboard</li>
							</ol>
						</div>
						<!-- /.col -->
					</div>
					<!-- /.row -->
				</div>
				<!-- /.container-fluid -->
			</div>
			<!-- /.content-header -->

			<!-- Main content -->
			<section class="content">
			<div class="container-fluid">
	    		<div class="row">
	    		
	    		<div class="col-lg-3 col-6">
            <!-- small card -->
            <div class="small-box bg-info">
              <div class="inner">
                <h3>150</h3>

                <p>Member</p>
              </div>
              <div class="icon">
                <i class="fas fa-user"></i>
              </div>
              <a href="member" class="small-box-footer">
                More info <i class="fas fa-arrow-circle-right"></i>
              </a>
            </div>
          </div>
         <!-- /.col -->
         
         <div class="col-lg-3 col-6">
            <!-- small card -->
            <div class="small-box bg-warning">
              <div class="inner">
                <h3>150</h3>

                <p>Invoice</p>
              </div>
              <div class="icon">
                <i class="fas fa-arrow-circle-up"></i>
              </div>
              <a href="invoice" class="small-box-footer">
                More info <i class="fas fa-arrow-circle-right"></i>
              </a>
            </div>
          </div>
   		 <!-- /.col -->
         
         <div class="col-lg-3 col-6">
            <!-- small card -->
            <div class="small-box bg-success">
              <div class="inner">
                <h3>140</h3>

                <p>Paid</p>
              </div>
              <div class="icon">
                <i class="fas fa-check-circle"></i>
              </div>
              <a href="billing" class="small-box-footer">
                More info <i class="fas fa-arrow-circle-right"></i>
              </a>
            </div>
          </div>
   		 <!-- /.col -->
   		 
   		  <div class="col-lg-3 col-6">
            <!-- small card -->
            <div class="small-box bg-danger">
              <div class="inner">
                <h3>10</h3>

                <p>Unpaid</p>
              </div>
              <div class="icon">
                <i class="fas fa-times-circle"></i>
              </div>
              <a href="billing" class="small-box-footer">
                More info <i class="fas fa-arrow-circle-right"></i>
              </a>
            </div>
          </div>
   		 <!-- /.col -->
   		 </div>
   		 <!-- /.row -->
   		 
   		  <div class="row">
	      <div class="col-md-6">
         <div class="card">
              <div class="card-header">
                <h3 class="card-title">Billing Payment</h3>
              </div>
              <!-- /.card-header -->
              <div class="card-body p-0">
                <table id="billingTable" class="table">
                  <thead>
                    <tr>
                      <th>Billing Name</th>
                      <th>Description</th>
                      <th>Payment</th>
                      <th style="width: 40px"></th>
                    </tr>
                  </thead>
                  <tbody>
                 	${lastBilling}
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
            </div>
            <!-- /.card -->
          </div>
          <!-- /.col -->
       
        <div class="col-md-6">
        <div class="card">
              <div class="card-header">
                <h3 class="card-title">Last Transaction</h3>
              </div>
              <!-- /.card-header -->
              <div class="card-body p-0">
                <table class="table table-striped">
                  <thead>
                    <tr>
                      <th>Date</th>
                      <th>From/To</th>
                      <th>Transaction</th>
                      <th>Amount</th>
                    </tr>
                  </thead>
                 	<tbody>${lastTrx}</tbody>
                </table>
              </div>
              <!-- /.card-body -->
            </div>
            <!-- /.card -->
          </div>
          <!-- /.col -->
             
        </div>
      <!-- /.row --> 
     	
      </div>
      <!-- /.container-fluid -->
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->
  
<!-- footer -->
	<%@include file="footer.jsp" %>
<!-- /.footer -->  