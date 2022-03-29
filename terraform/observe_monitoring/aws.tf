module "observe_kinesis_firehose" {
  source = "github.com/observeinc/terraform-aws-kinesis-firehose//eks?ref=main"

  observe_customer = var.observe_customer
  observe_token    = var.observe_token
  observe_domain   = var.observe_domain

  eks_cluster_name = "observe-demo-cluster"
  pod_execution_role_arns = [
    "arn:aws:iam::739672403694:role/default-20220329174112884800000007",
  ]
}