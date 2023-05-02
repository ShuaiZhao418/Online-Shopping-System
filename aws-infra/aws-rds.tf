resource "aws_db_instance" "csye6225" {
  identifier           = "csye6225"

  allocated_storage    = 10
  engine               = "mysql"
  engine_version       = "5.7"
  instance_class       = "db.t3.micro"
  db_name              = "csye6225"
  username             = "csye6225"
  password             = "123456ZX??"
  parameter_group_name = "${aws_db_parameter_group.example.name}"
  db_subnet_group_name = aws_db_subnet_group.example.name
  vpc_security_group_ids = [aws_security_group.database.id]
  multi_az             = false
  publicly_accessible  = false
  skip_final_snapshot  = true
  kms_key_id = aws_kms_key.example.arn

  storage_type = "gp2"
  storage_encrypted = true

  tags = {
    Name = "Example DB"
  }
}


