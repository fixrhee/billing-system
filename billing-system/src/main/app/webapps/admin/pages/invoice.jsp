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
            <h1>Invoice</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="index">Home</a></li>
              <li class="breadcrumb-item active">Invoice</li>
            </ol>
          </div>
        </div>
   
      </div><!-- /.container-fluid -->
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header">
              <h3 class="card-title">All Invoice</h3>
              <div align="right"><%= (new java.util.Date()).toLocaleString()%></div>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
              <table id="example2" class="table table-bordered table-striped responsive nowrap">
                <thead>
                <tr>
                  <th>Invoice Number</th>
                  <th>Name</th>
                  <th>Billing</th>
                  <th>Amount</th>
                  <th>Status</th>
                  <th>Action</th>
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
 		$("#example2")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "bSort" : false,
       			     "ajax" : {
       					 "url" : "invoiceData"
 		              },
 			     	 "columns" : [{
								"data" : "invoiceNo"
							}, {
								"data" : "name"
							}, {
								"data" : "billing"
							}, {
								"data" : "amount"
							}, {
								"data" : "status"
							}, {
								"data" : "id",
								"render" : function ( data, type, row ) {
        							 return "<button type='button' class='btn btn-default btn-sm' data-toggle='tooltip' title='View Detail' onclick='viewDetail(" + data + ");'/><i class='fa fa-info-circle'></i> " + 
                   				" <button type='button' class='btn btn-default btn-sm' data-toggle='tooltip' title='Edit' /><i class='far fa-edit'></i>" + 
                   				" <button type='button' class='btn btn-default btn-sm' data-toggle='tooltip' title='View Invoice'  onclick='viewInvoice(" + data + ");'/><i class='far fa-eye'></i>";
               					 }	
							}]
					});
	</script>
	
<script>
function viewInvoice(id){
	window.location.href='viewInvoice?invoiceID=' + id;
};
</script>

<script>
function viewDetail(id){
	window.location.href='invoiceDetail?invoiceID=' + id;
};
</script>	

<c:if test="${not empty fn:trim(notification)}">	
	<script type="text/javascript">
	$(function(){
 	 new PNotify({
        title: '${title}',
        text: '${message}',
        type: '${notification}',
        styling: 'bootstrap3'
        });
	});
	</script>
</c:if>
</body>
</html>
