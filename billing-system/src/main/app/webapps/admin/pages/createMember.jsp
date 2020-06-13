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
            <h1>New Member</h1>
          </div>
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="index">Home</a></li>
              <li class="breadcrumb-item active"><a href="member">Member</a></li>
              <li class="breadcrumb-item active">Create Member</li>
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
           <nav>
					<div class="nav nav-tabs nav-fill" id="nav-tab" role="tablist">
						<a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true">Single Form</a>
						<a class="nav-item nav-link" id="nav-profile-tab" data-toggle="tab" href="#nav-profile" role="tab" aria-controls="nav-profile" aria-selected="false">Bulk</a>
					</div>
				</nav>
 	        <!-- /.card-header -->
        
            	<div class="tab-content py-3 px-3 px-sm-0" id="nav-tabContent">
				<div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
	    
            <div class="card-body">
               <form action="submitMember" class="form-horizontal" method="POST">
                  <div class="form-group">
                    <label for="name" class="col-sm-2 col-form-label">Name</label>
                    <div class="col-sm-12">
                      <input type="text" class="form-control" id="name" name="name" required placeholder="Name">
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="mobile" class="col-sm-2 col-form-label">Mobile</label>
                    <div class="col-sm-12">
                      <input type="text" class="form-control" id="msisdn" name="msisdn" required placeholder="Mobile No">
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="email" class="col-sm-2 col-form-label">Email</label>
                    <div class="col-sm-12">
                      <input type="text" class="form-control" id="email" name="email" placeholder="Email">
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="address" class="col-sm-2 col-form-label">Address</label>
                    <div class="col-sm-12">
                      <textarea class="form-control" rows="3" name="address" id="address" placeholder="Address"></textarea>
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="idcard" class="col-sm-2 col-form-label">ID Card</label>
                    <div class="col-sm-12">
                      <input type="text" class="form-control" id="idcard" name="idcard" placeholder="ID Card">
                    </div>
                  </div>
 
                </div>
                <hr />
                <!-- /.card-body -->
                <div class="card-footer">
                <div align="right">
                  <button type="submit" class="btn btn-info">Submit</button>
                  <button type="reset" class="btn btn-default">Reset</button>
                  </div>
                </div>
                <!-- /.card-footer -->
              </form>
               </div>
            
            		<div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">
			         <div class="card-body">
            <div class="card-body">
            <form action="submitBulkMember" class="form-horizontal" method="POST" enctype="multipart/form-data">
     		  <div class="form-group">
                    <label for="memberBulk">File input</label>
                    <div class="input-group">
                      <div class="custom-file">
                        <input type="file" class="custom-file-input" id="memberBulk" name="memberBulk" required />
                        <label class="custom-file-label" for="memberBulk">Choose file</label>
                      </div>
                    </div>
                  </div>
                      Please upload a CSV format based file, you can download the template <a href="download/MemberTemplate.csv">here</a>
                  </div>

				</div>
        		<!-- /.tab -->
        		<hr />
             <div class="card-footer">
                  <div align="right"><button type="submit" class="btn btn-info">Upload</button></div>
                </div>
                <!-- /.card-footer -->
			</form>
            </div>
            <!-- /.tab All -->
            
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

</body>
</html>
