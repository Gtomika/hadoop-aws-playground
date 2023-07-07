resource "aws_s3_bucket" "hadoop-playground-bucket" {
  bucket = var.bucket_name
  force_destroy = true
}

resource "aws_s3_object" "input_file" {
  bucket = aws_s3_bucket.hadoop-playground-bucket.bucket
  key    = "input/input.txt"
  source = "../input.txt"
}

# build JAR file with gradle JAR job first
resource "aws_s3_object" "jar_file" {
  bucket = aws_s3_bucket.hadoop-playground-bucket.bucket
  key    = "jars/word_counter.jar"
  source = "../build/libs/HadoopPlayground-1.0-SNAPSHOT.jar"
}

resource "aws_emr_cluster" "hadoop_playground_cluster" {
  name          = "HadoopPlayground"
  release_label = "emr-6.9.0"
  service_role  = aws_iam_role.iam_emr_service_role.arn
  applications = ["Hadoop"]

  ec2_attributes {
    subnet_id                         = aws_subnet.main.id
    emr_managed_master_security_group = aws_security_group.allow_access.id
    emr_managed_slave_security_group  = aws_security_group.allow_access.id
    instance_profile                  = aws_iam_instance_profile.emr_profile.arn
  }

  master_instance_group {
    instance_type = "m5.xlarge"
  }

  core_instance_group {
    instance_count = 1
    instance_type  = "m5.xlarge"
  }

  log_uri = "s3://${aws_s3_bucket.hadoop-playground-bucket.bucket}/logs/"

  configurations_json = <<EOF
  [
    {
      "Classification": "hadoop-env",
      "Configurations": [
        {
          "Classification": "export",
          "Properties": {
            "JAVA_HOME": "/usr/lib/jvm/java-1.8.0"
          }
        }
      ],
      "Properties": {}
    }
  ]
EOF
}
