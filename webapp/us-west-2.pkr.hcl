variable "aws_region" {
  type    = string
  default = "us-west-2"
}

variable "source_ami" {
  type    = string
  default = "ami-0f1a5f5ada0e7da53"
}

variable "ssh_username" {
  type    = string
  default = "ec2-user"
}

variable "subnet_id" {
  type    = string
  default = "subnet-0c4a93fc096edeb69"
}

variable "aws_profile" {
  description = "AWS Provider Profile"
  type        = string
  default     = "dev"
}

variable "demo_account_id" {
  description = "demo_account_id"
  type        = string
  default     = "502031001009"
}

source "amazon-ebs" "my-ami" {
  region          = "${var.aws_region}"
  ami_name        = "csye6225_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  ami_description = "AMI for CSYE 6225 us-west-2"
  ami_users = ["${var.demo_account_id}"]
  profile = var.aws_profile
  ami_regions = [
    "us-west-2"
  ]
  aws_polling {
    delay_seconds = 120
    max_attempts  = 50
  }

  instance_type               = "t2.micro"
  source_ami                  = "${var.source_ami}"
  ssh_username                = "${var.ssh_username}"
  subnet_id                   = "${var.subnet_id}"
  associate_public_ip_address = true

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/xvda"
    volume_size           = 8
    volume_type           = "gp2"
  }
}

build {
  sources = ["source.amazon-ebs.my-ami"]
  provisioner "file" {
    source      = "installdatabase.sh"
    destination = "/tmp/installdatabase.sh"
  }
  provisioner "file" {
    source      = "installmaven.sh"
    destination = "/tmp/installmaven.sh"
  }
  provisioner "file" {
    source      = "installCloudWatch.sh"
    destination = "/tmp/installCloudWatch.sh"
  }
  provisioner "file" {
    source      = "cloudwatch-config.json"
    destination = "/tmp/cloudwatch-config.json"
  }
  provisioner "file" {
    source      = "application.sh"
    destination = "/tmp/application.sh"
  }
  provisioner "file" {
    source      = "Webapplication/target/MyWebApplication.jar"
    destination = "/tmp/MyWebApplication.jar"
  }
  provisioner "file" {
    source      = "webapp.service"
    destination = "/tmp/webapp.service"
  }
  provisioner "shell" {
    environment_vars = [
      "DEBIAN_FRONTEND=noninteractive",
      "CHECKPOINT_DISABLE=1"
    ]
    inline = [
      // give permission to all
      "cd /tmp",
      "chmod 777 installdatabase.sh",
      "chmod 777 installmaven.sh",
      "chmod 777 application.sh",
      "chmod 777 installCloudWatch.sh",
      "chmod 777 webapp.service",
      "sudo cp /tmp/webapp.service /etc/systemd/system/",
      // install mariadb and change passowrd
      "./installdatabase.sh",
      // install maven
      "./installmaven.sh",
      // install Cloud Watch
      "./installCloudWatch.sh",
      // run application.sh
      "./application.sh"
    ]
  }
}



