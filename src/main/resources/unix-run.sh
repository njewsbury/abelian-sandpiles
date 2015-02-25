#!/bin/bash
#######################################################
# ABELIAN SANDPILE BEHABIOUR MODEL
# PHYSICS 4250 - ASSIGNMENT # ONE
#
# AUTHOR : NATHAN JEWSBURY
#
# The purpose of this file is to define a series
# of simulation parameters to run the created
# sandpile model simulation JAR file.  The below
# simulation paramaters may be changed to fit
# individual requirements.  
#
# The simulation will run for each defined parameter
# (stable step count & grid size) and each simulation
# will be repeated 'SIMULATION_TRIAL_TIMES' times.
#
#######################################################

############ DEFINE SIMULATION PARAMETERS ##############
## How many steps to complete after reaching 'stable' state.
### Array elements define a unique trial.
SIMULATION_STABLE_STEPS=(1000 10000)

## How large should the sandbox grid be
### Array elements define a unique trial.
SIMULATION_GRID_SIZE=(101)

## How many simulations to run for each 'trial'.
SIMULATION_TRIAL_TIMES=2

## How often should the program output an image.
### this will output images even before the stable state.
### (an image will also be saved after the simulation is complete.)
SIMULATION_OUTPUT_IMAGE_EVERY_N=1000000

## The sandbox model to test, two options are ABELIAN and REALISTIC.
### Uncomment the desired model, comment out the non-desired.
SIMULATION_SANDBOX_MODEL="ABELIAN"
#SIMULATION_SANDBOX_MODEL="REALISTIC"

## Unique ID for image-generation.
### I used this to distinguish use of AMD/Intel CPUs.
SIMULATION_CPU_ID="PHYS"

########################################################
# Simulation Constants -- Do not alter.
SIMULATION_RESULT_HEADER="Output Format ((**TRANST == TRANSIENT**)):\n[CPU TYPE]\t[MODEL TYPE]\t[GRID SIZE]\t[STABLE STEPS]\t[TRIAL ID]\n[TRANSNT TIME]\t[TRANSNT STEPS]\t[TOTAL STEPS]\t[TOTAL TIME]\t[MAXIMUM AVALANCHE]"
SPACER="-----------------------------------------------------------------------------------"
REQUIRED_TERMINAL_WIDTH=100
########################################################
function run_jar_file {
  local trial=$1
  local cpu=$2
  local grid=$3
  local steps=$4 
  local model=$5 
  local imgPrint=$6

  ## Required ARGS [trial][cpu][grid][steps][model][imgPrint]
  java -jar njewsbury-sandpile-model.jar $trial $cpu $grid $steps $model $imgPrint

}

function main {
  if [[ ( $COLUMNS < $REQUIRED_TERMINAL_WIDTH) ]]; then
    echo -e "Starting NJewsbury Sandpile Simulation v1.0"
    echo -e $SIMULATION_RESULT_HEADER
    echo $SPACER
    local trialId=1
    local model=$SIMULATION_SANDBOX_MODEL
    local imgEvery=$SIMULATION_OUTPUT_IMAGE_EVERY_N
    for gridSize in "${SIMULATION_GRID_SIZE[@]}"; do
      if [[ (gridSize > 0) ]]; then
	for stepCount in "${SIMULATION_STABLE_STEPS[@]}"; do
	  local i=0
	  while [[ ($i < $SIMULATION_TRIAL_TIMES) ]]; do
	    run_jar_file $trialId $SIMULATION_CPU_ID $gridSize $stepCount $model $imgEvery 
            trialId=$((trialId+1))
	    i=$((i+1))
	  done
	done
      else
	echo "Invalid grid size '$gridSize'."
      fi
	#Required args are [trial] [cpu] [grid] [steps] [model] [imgPrint]
	#java -jar njewsbury-sandpile-model.jar $trial $cpuId $gridSize $stepCount $model $imgEvery
    
    done
  else
      echo "You must increase your terminal window to be at least $REQUIRED_TERMINAL_WIDTH columns wide."
  fi
}

main

exit 0;
