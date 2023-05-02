resource "aws_autoscaling_group" "asg" {
    name = "example-asg"

    min_size             = 1
    max_size             = 3
    desired_capacity     = 1
    default_cooldown     = 60

    vpc_zone_identifier = [aws_subnet.public_subnets[0].id, aws_subnet.public_subnets[1].id, aws_subnet.public_subnets[2].id]

    tag {
        key = "CSYE6225"
        value = "CSYE6225"
        propagate_at_launch = true
    }

    launch_template {
        id = aws_launch_template.lt.id
        version = "$Latest"
    }

    target_group_arns = [
        aws_lb_target_group.alb_tg.arn
    ]
}

resource "aws_autoscaling_policy" "scale_up_policy" {
  name                   = "scale-up-policy"
  policy_type            = "SimpleScaling"
  autoscaling_group_name = aws_autoscaling_group.asg.name
  adjustment_type = "ChangeInCapacity"
  scaling_adjustment = "1"
}

resource "aws_cloudwatch_metric_alarm" "cpu-alarm-scale-up" {
    alarm_name = "example-cpu-alarm1"
    alarm_description = "example-cpu-alarm"
    comparison_operator = "GreaterThanThreshold"
    evaluation_periods = "2"
    metric_name = "CPUUtilization"
    namespace = "AWS/EC2"
    period = "60"
    statistic = "Average"
    threshold = "5"
    dimensions = {
      "AutoScalingGroupName" = "${aws_autoscaling_group.asg.name}"
    }
    actions_enabled = true
    alarm_actions = ["${aws_autoscaling_policy.scale_up_policy.arn}"]
}

resource "aws_autoscaling_policy" "scale_down_policy" {
  name                   = "scale-down-policy"
  policy_type            = "SimpleScaling"
  autoscaling_group_name = aws_autoscaling_group.asg.name
  adjustment_type = "ChangeInCapacity"
  scaling_adjustment = "-1"
}

resource "aws_cloudwatch_metric_alarm" "cpu-alarm-scale-down" {
    alarm_name = "example-cpu-alarm2"
    alarm_description = "example-cpu-alarm"
    comparison_operator = "LessThanThreshold"
    evaluation_periods = "2"
    metric_name = "CPUUtilization"
    namespace = "AWS/EC2"
    period = "60"
    statistic = "Average"
    threshold = "3"
    dimensions = {
      "AutoScalingGroupName" = "${aws_autoscaling_group.asg.name}"
    }
    actions_enabled = true
    alarm_actions = ["${aws_autoscaling_policy.scale_down_policy.arn}"]
}

