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
            <h1>Transaction History</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="index">Home</a></li>
              <li class="breadcrumb-item active"><a href="transactionHistory">Transaction History</a></li>
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
              <h3 class="card-title">Transaction History</h3>
              <div class="card-tools">
                 <div class="input-group-prepend">
              <div id="reportrange" class="pull-right" style="background: #fff; cursor: pointer; padding: 5px 10px; border: 1px solid #ccc">
                      <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
                      <span></span> <b class="caret"></b>
                    </div> <div class="input-group-append"><button class="btn btn-default" type="button" id="saveBtn" name="saveBtn" onclick="reloadTable();"><i class="fas fa-search"></i></button></div>
              
              </div>
                  <!-- div class="input-group">
                    <div class="input-group-prepend">
                      <span class="input-group-text">
                        <i class="far fa-calendar-alt"></i>
                      </span>
                    </div>
                    <input type="text" class="form-control float-right" size="22" id="dateHistory">
                    <div class="input-group-append">
                     <div class="btn btn-primary" onclick="reloadTable();">
                      <i class="fas fa-search"></i>
                    </div>
                  </div>
                  </div -->
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
                  <th>From/To</th>
                  <th>Transaction</th>
                  <th>Amount</th>
                  <th>Trace Number</th>
                  <th>Description</th>
                  <th>Transaction Number</th>
                </tr>
                </thead>
              </table>
                <input  type="hidden" name="from" id="from" value="2020-07-01" /> 
				<input  type="hidden" name="to" id="to" value="2020-07-30" />    
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
		

<script type="text/javascript">
$(document).ready(function() {
    $('#reportrange').daterangepicker(
       {
          startDate: moment().subtract('days', 29),
          endDate: moment(),
          //minDate: '01/01/2012',
          //maxDate: '12/31/2014',
          dateLimit: { days: 60 },
          showDropdowns: true,
          showWeekNumbers: true,
          timePicker: false,
          timePickerIncrement: 1,
          timePicker12Hour: true,
          ranges: {
             'Today': [moment(), moment()],
             'Yesterday': [moment().subtract('days', 1), moment().subtract('days', 1)],
             'Last 7 Days': [moment().subtract('days', 6), moment()],
             'Last 30 Days': [moment().subtract('days', 29), moment()],
             'This Month': [moment().startOf('month'), moment().endOf('month')],
             'Last Month': [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')]
          },
          opens: 'left',
          buttonClasses: ['btn btn-default'],
          applyClass: 'btn-small btn-primary',
          cancelClass: 'btn-small',
          format: 'DD/MM/YYYY',
          separator: ' to ',
          locale: {
              applyLabel: 'Submit',
              fromLabel: 'From',
              toLabel: 'To',
              customRangeLabel: 'Custom Range',
              daysOfWeek: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr','Sa'],
              monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
              firstDay: 1
          }
       },
       function(start, end) {
        $('#reportrange span').html(start.format('D MMMM YYYY') + ' - ' + end.format('D MMMM YYYY'));
        document.getElementById("from").value = start.format('YYYY-MM-DD');
        document.getElementById("to").value = end.format('YYYY-MM-DD');
       }
    );
    
    //Set the initial state of the picker label
    $('#reportrange span').html(moment().subtract('days', 29).format('D MMMM YYYY') + ' - ' + moment().format('D MMMM YYYY'));
    
 });
</script> 
 
 <script>
 		$("#memberTable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "bFilter": false,
       				 "bSort" : false,
       			     "ajax" : {
       					 "url" : "transactionHistoryData",
       					   "data" : function ( d ) {
 		            	  		d.startDate = document.getElementById("from").value;
 		            	  		d.endDate = document.getElementById("to").value;
 			            	}
 		              },
 			     	 "columns" : [{
								"data" : "date"
							}, {
								"data" : "member"
							}, {
								"data" : "transaction"
							}, {
								"data" : "amount"
							}, {
								"data" : "traceNo"
							}, {
								"data" : "description"
							}, {
								"data" : "transactionNumber"
							}]
					});
</script>

<script>
  function reloadTable(){
   var table = $('#memberTable').DataTable();
		table.ajax.reload( null, false );
};
</script>
</body>
</html>
