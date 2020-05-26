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
            <h1>New Voucher</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="index">Home</a></li>
              <li class="breadcrumb-item active"><a href="voucher">Voucher</a></li>
              <li class="breadcrumb-item active">Create Voucher</li>
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
            <!-- /.card-header -->
            <div class="card-body">
         	   <form role="form">	
     			<div class="col-sm-6">
                      <!-- select -->
                      <div class="form-group">
                        <label>Select Product</label>
                        <select class="form-control">
           	             	<option>---- Select Product ----</option>
		                <c:forEach var="listProduct" items="${listProduct}">
							<option name="productName" id="productName" value="${listProduct.id}">${listProduct.name}</option>
						</c:forEach>
	             </select>
                      </div>
                    </div>
                  
                    <div class="form-group">
                    <label for="code" class="col-sm-2 col-form-label">Voucher Code</label>
                    <div class="col-sm-12">
                      <input type="text" class="form-control" id="code" placeholder="Code">
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label for="exampleInputFile">Voucher Image</label>
                    <div class="input-group">
                      <div class="custom-file">
                        <input type="file" class="custom-file-input" id="exampleInputFile">
                        <label class="custom-file-label" for="exampleInputFile">Choose file</label>
                      </div>
                    </div>
                    
                  <br/>
                  <br/>
                  <hr/>
                  <br/>
                  
                  <div class="form-group">
                    <label for="name" class="col-sm-2 col-form-label">Name</label>
                    <div class="col-sm-12">
                      <input type="text" class="form-control" id="name" placeholder="Name">
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="description" class="col-sm-2 col-form-label">Description</label>
                    <div class="col-sm-12">
                      <input type="text" class="form-control" id="description" placeholder="Description">
                    </div>
                  </div>
                  
                   <div class="form-group">
                    <label for="quota" class="col-sm-2 col-form-label">Quota</label>
                    <div class="col-sm-12">
                      <input type="text" class="form-control" id="quota" placeholder="Quota">
                    </div>
                  </div>
    				
 					<div class="form-group">
                    		<label for="percentage" class="col-sm-2 col-form-label">Amount</label>
                    <div class="input-group mb-3">
            		   <div class="input-group-prepend">
                    <span class="input-group-text">Rp.</span>
                  </div>
                     <input type="text" class="form-control">
                    </div>
                  </div>
 
                    <div class="form-group">
                    		<label for="percentage" class="col-sm-2 col-form-label">Percentage</label>
                    <div class="input-group mb-3">
                     <input type="text" class="form-control">
                 	<div class="input-group-append">
                		    <span class="input-group-text">%</span>
              	    </div>
                    </div>
                  </div>
               
                  <br/>
                  <br/>
                  <hr/>
                  <br/>
                  
                 <div class="form-group">
                  <label>Publish Expired Date</label>
                    <div class="input-group date" id="publishdate" data-target-input="nearest">
                        <input type="text" id="publishdatepicker" name="publishdatepicker" class="form-control datetimepicker-input" data-target="#publishdate"/>
                        <div class="input-group-append" data-target="#publishdate" data-toggle="datetimepicker">
                            <div class="input-group-text"><i class="fa fa-calendar"></i></div>
                        </div>
                    </div>
                </div>
                
                <div class="form-group">
                  <label>Redeem Expired Date</label>
                    <div class="input-group date" id="redeemdate" data-target-input="nearest">
                        <input type="text" class="form-control datetimepicker-input" data-target="#redeemdate"/>
                        <div class="input-group-append" data-target="#redeemdate" data-toggle="datetimepicker">
                            <div class="input-group-text"><i class="fa fa-calendar"></i></div>
                        </div>
                    </div>
                </div>
       
                <div class="card-footer">
                  <button type="submit" class="btn btn-info">Submit</button>
                  <button type="reset" class="btn btn-default">Reset</button>
                </div>
        
               </form>
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
  $(function() {
    // Bootstrap DateTimePicker v3
    $('#publishdatepicker').datetimepicker({
      pickTime: false
    });
    // Bootstrap DateTimePicker v4
    $('#publishdatepicker').datetimepicker({
      format: 'YYYY-MM-DD'
    });
  });
</script>

</body>
</html>
