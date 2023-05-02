data "template_file" "user_data" {

    template = <<EOF

        #!/bin/bash

        echo "DB_USERNAME=${aws_db_instance.csye6225.username}" >> /etc/environment
        echo "DB_PASSWORD=${aws_db_instance.csye6225.password}" >> /etc/environment
        echo "DB_NAME=${aws_db_instance.csye6225.db_name}" >> /etc/environment
        echo "DB_ENPOINT=${aws_db_instance.csye6225.endpoint}" >> /etc/environment
        echo "S3_BUCKET_NAME=${aws_s3_bucket.my_bucket.bucket}" >> /etc/environment

        source /etc/environment

        sudo /opt/aws/amazon-cloudwatch-agent/bin/amazon-cloudwatch-agent-ctl \
        -a fetch-config \
        -m ec2 \
        -c file:/tmp/cloudwatch-config.json \
        -s

        cd /tmp
        sudo systemctl daemon-reload
        sudo systemctl enable webapp.service
        sudo systemctl start webapp.service

    EOF

}

resource "aws_launch_template" "lt" {
    name = "asg_launch_config"
    image_id = var.ami_id
    instance_type = "t2.micro"
    key_name = aws_key_pair.deployer.key_name

    # vpc_security_group_ids = ["${aws_security_group.example.id}"]
    
    network_interfaces {
        associate_public_ip_address = true
        security_groups = [aws_security_group.example.id]
    }

    iam_instance_profile {
        name = aws_iam_instance_profile.example.name
    }

    block_device_mappings {
        device_name = "/dev/xvda"
        ebs {
            volume_size = 8
            volume_type = "gp2"
            encrypted   = true
            kms_key_id = aws_kms_key.example.arn
            delete_on_termination = true
        }
    }

    user_data = base64encode(data.template_file.user_data.rendered)
}
