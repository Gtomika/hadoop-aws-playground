terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.60.0"
    }
  }

  # alternatively use backend "local"
  backend "s3" {}
}

provider "aws" {
  access_key = var.aws_key_id
  secret_key = var.aws_secret_key
  region = var.aws_region

  # can be enabled if you like roles more then fixed credentials for Terraform
#  assume_role {
#    role_arn = var.aws_terraform_role_arn
#    external_id = var.aws_terraform_role_external_id
#    duration = "1h"
#    session_name = "HadoopPlaygroundTerraformSession"
#  }

  default_tags {
    tags = {
      application = "HadoopPlayground"
      managed_by = "Terraform"
      owner = "Tamas Gaspar"
    }
  }
}