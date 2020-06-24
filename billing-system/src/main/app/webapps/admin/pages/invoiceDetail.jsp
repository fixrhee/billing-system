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
            <h1>Invoice Detail</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="index">Home</a></li>
              <li class="breadcrumb-item"><a href="invoice">Invoice</a></li>
              <li class="breadcrumb-item active">Detail</li>
            </ol>
          </div>
        </div>  
      </div><!-- /.container-fluid -->
    </section>

    <!-- Main content -->
    <section class="content">
      
      <div class="row">
        <div class="col-md-12">
          <div class="card">
      		 <div class="card-header">
              <h3 class="card-title">Invoice History</h3>
              <div class="card-tools">
              
                  <div class="input-group">
                    <div class="input-group-prepend">
                      <span class="input-group-text">
                        <i class="far fa-calendar-alt"></i>
                      </span>
                    </div>
                    <input type="text" class="form-control float-right" size="22" id="reservation">
                    <div class="input-group-append">
                     <div class="btn btn-primary" onclick="searchFilter(${billingID});">
                      <i class="fas fa-search"></i>
                    </div>
                  </div>
                  </div>
                  <!-- /.input group -->
              </div>
              <!-- /.card-tools -->
            </div>
            <!-- /.card-header -->   
            
            <div class="card-body">
               <table id="memberTable" class="table table-bordered table-striped responsive nowrap">
                <thead>
                <tr>
                  <th>Date</th>
                  <th>Invoice Number</th>
                  <th>Amount</th>
                  <th>Status</th>
                </tr>
                </thead>
              </table>
                            
            </div>
            <!-- /.card-body -->        
            </div>        
          <!-- /.card -->   
           </div>
        <!-- /.col -->
            
      </div>
      <!-- /.row -->           
      
    </section>
    <!-- /.content -->
  </div>
  <!-- /.content-wrapper -->

		<!-- footer -->
			<%@include file="footer.jsp" %>
		<!-- /.footer -->
		
<script>
 		$("#memberTable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "bFilter": true,
       				 "bSort" : false,
       			     "ajax" : {
       					 "url" : "billingStatusData",
       					  data: { 
      					  "billingID": ${billingID}, 
     					  "filter": ${filter}
       					  }
 		              },
 			     	 "columns" : [{
								"data" : "username"
							}, {
								"data" : "name"
							}, {
								"data" : "invoice"
							}, {
								"data" : "amount"
							}, {
								"data" : "status"
							}]
					});
</script>


<script>
  $(function () {
  
    //Datemask dd/mm/yyyy
    $('#datemask').inputmask('dd/mm/yyyy', { 'placeholder': 'dd/mm/yyyy' })
    //Datemask2 mm/dd/yyyy
    $('#datemask2').inputmask('mm/dd/yyyy', { 'placeholder': 'mm/dd/yyyy' })
    //Money Euro
    $('[data-mask]').inputmask()

    //Date range picker
    $('#reservation').daterangepicker({
    showDropdowns: true,
    startDate: moment().subtract(29, 'days'),
    endDate  : moment(),
    locale: { 
        format: 'YYYY-MM-DD'
    		}
    })
    //Date range picker with time picker
   
    //Date range as a button
    $('#daterange-btn').daterangepicker(
      {
        ranges   : {
          'Today'       : [moment(), moment()],
          'Yesterday'   : [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
          'Last 7 Days' : [moment().subtract(6, 'days'), moment()],
          'Last 30 Days': [moment().subtract(29, 'days'), moment()],
          'This Month'  : [moment().startOf('month'), moment().endOf('month')],
          'Last Month'  : [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        },
        startDate: moment().subtract(29, 'days'),
        endDate  : moment()
      },
      function (start, end) {
        $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'))
      }
    )
  })
</script>
</body>
</html>
