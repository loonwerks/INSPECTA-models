generated_dir=$1 #generated location
concrete_file_name=$2
workspace_dir=$generated_dir"/aadl"
codegen_dir=$generated_dir"/hamr/microkit" # generated code location
attestation_dir=$codegen_dir"/attestation"

request_skeleton_path=$attestation_dir"/aadl_appraise.json"

sed 's#%%codegen_dir%%#'$codegen_dir'#g' $request_skeleton_path | \
sed 's#%%attestation_dir%%#'$attestation_dir'#g' | \
sed 's#%%workspace_dir%%#'$workspace_dir'#g' > $concrete_file_name

env_path=$AM_REPOS_ROOT"/rust-am-clients/rodeo_configs/rodeo_envs/env_rodeo_micro.json"

rodeo_exe=$AM_REPOS_ROOT"/rust-am-clients/target/release/rust-rodeo-client"

$rodeo_exe -r $concrete_file_name -e $env_path


