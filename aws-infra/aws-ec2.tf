# resource "aws_instance" "my-server" {
#   ami                    = var.ami_id
#   instance_type          = "t2.micro"
#   key_name               = aws_key_pair.deployer.key_name
#   vpc_security_group_ids = ["${aws_security_group.example.id}"]
#   subnet_id = aws_subnet.public_subnets[0].id
#   associate_public_ip_address = true
#   iam_instance_profile = aws_iam_instance_profile.example.name
#   user_data = <<EOF

#     #!/bin/bash

#     echo "DB_USERNAME=${aws_db_instance.csye6225.username}" >> /etc/environment
#     echo "DB_PASSWORD=${aws_db_instance.csye6225.password}" >> /etc/environment
#     echo "DB_NAME=${aws_db_instance.csye6225.db_name}" >> /etc/environment
#     echo "DB_ENPOINT=${aws_db_instance.csye6225.endpoint}" >> /etc/environment
#     echo "S3_BUCKET_NAME=${aws_s3_bucket.my_bucket.bucket}" >> /etc/environment

#     source /etc/environment

#     sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl \
#     -a fetch-config \
#     -m ec2 \
#     -c file:/tmp/cloudwatch-config.json \
#     -s

#     cd /tmp
#     sudo systemctl daemon-reload
#     sudo systemctl enable webapp.service
#     sudo systemctl start webapp.service


#   EOF

#   tags = {
#     Name = "example-instance"
#   }
# }
