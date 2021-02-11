<template>
  <div class="training-page">
    <div>
      <Menu page="/training" name="brais" />
    </div>
    <h1>Training</h1>

    <!-- Train menu -->
    <div id="train_menu">
      <form class="vue-form train_form" @submit.prevent="train">
        <fieldset>
          <input
            class="train_button"
            type="submit"
            value="Train"
          />
          <v-combobox
            class="combo_select_db"
            required
            v-model="database_selected"
            :items="database_names"
            placeholder="Select a database"
          >
          </v-combobox>
          <v-combobox
            class="combo_select_model"
            required
            v-model="model_selected"
            :items="model_names"
            placeholder="Select a model"
          >
          </v-combobox>
        </fieldset>
        </form>
        
    </div>

    <!-- Configuration parameters / Admin models -->
    <div id="config_admin">
      <!-- Configuration parameters -->
      <div id="config_parameters">
        <form class="vue-form config_parameters_form">
          <fieldset>
            <label><h2>Configuration Parameters</h2></label>
            <input
              type="text"
              placeholder="Model name"
              name="model_name_input"
              id="model_name_input"
              required
              v-model="config_parameters.model_name"
            />
            <input
              type="number"
              step="0.001"
              name="learning_rate_input"
              id="learning_rate_input"
              v-model="config_parameters.learning_rate"
              placeholder="Learning rate"
              controls
            />
            <input
              type="number"
              step="1"
              required
              name="epochs_input"
              id="epochs_input"
              v-model="config_parameters.epochs"
              placeholder="Epochs"
              controls
            />
            <input
              type="number"
              step="0.001"
              name="epsilon_input"
              id="epsilon_input"
              v-model="config_parameters.epsilon"
              placeholder="Epsilon"
              controls
            />
            <input
              type="number"
              step="1"
              required
              name="batch_size_input"
              id="batch_size_input"
              v-model="config_parameters.batch_size"
              placeholder="Batch size"
              controls
            />
            <input
              type="number"
              step="0.001"
              name="decay_input"
              id="decay_input"
              v-model="config_parameters.decay"
              placeholder="Decay"
              controls
            />
            <input
              type="number"
              step="1"
              required
              name="train_size_input"
              id="train_size_input"
              v-model="config_parameters.train_size"
              placeholder="Train size"
              controls
            />
            <input
              type="number"
              step="0.001"
              name="validation_size_input"
              id="validation_size_input"
              v-model="config_parameters.validation_size"
              placeholder="Validation size"
              controls
            />
            <v-combobox
              class="target_input"
              required
              name="target_input"
              v-model="config_parameters.target_selected"
              :items="config_parameters.target"
              placeholder="Select the target"
            >
            </v-combobox>
            <v-combobox
              class="optimizer_input"
              required
              name="optimizer_input"
              v-model="config_parameters.optimizer_selected"
              :items="config_parameters.optimizer"
              placeholder="Select the optimizer"
            >
            </v-combobox>
            <v-combobox
              class="loss_input"
              required
              name="loss_input"
              v-model="config_parameters.loss_selected"
              :items="config_parameters.loss"
              placeholder="Select the loss function"
            >
            </v-combobox>
            <v-combobox
              class="metrics_input"
              required
              name="metrics_input"
              v-model="config_parameters.metrics_selected"
              :items="config_parameters.metrics"
              placeholder="Select the metric"
            >
            </v-combobox>
          </fieldset>
        </form>
      </div>

      <!-- Admin models -->
      <div id="admin_models">
        <form class="vue-form sel_db_form" @submit.prevent="deleteDB">
          <fieldset>
            <input
              class="delete_button"
              type="submit"
              value="Delete the model"
            />
            <v-combobox
              class="combo_models"
              required
              v-model="model_selected"
              :items="model_names"
              placeholder="Select a model"
            >
            </v-combobox>
            <div class="drop-zone">
              <b-img
                class="db_image"
                :src="getImgNN()"
                alt="Model image"
              ></b-img>
            </div>
          </fieldset>
        </form>

        <div id="layers">
          <label><h3>Layers</h3></label>

          <div class="border_rect" id="layers_names">
            <div class="container" v-for="(layer, idx) in layers" :key="idx">
              <div :id="layer.name" class="layer">
                {{  layer.name  }}          
              </div>
            </div>
          </div>

        </div>
            
      </div>
    </div>

    <!-- Desing Palette -->
    <div id="design_palette">
      <div id="palette">
      <h2>Design Palette</h2>
        <form
          class="vue-form palette_form"
          @submit.prevent="generateNewConfigFile()"
        >
          <fieldset>
            <input
              class="cf_button"
              type="submit"
              value="Generate config file"
            />
          </fieldset>
        </form>
        <div class="border_rect">
          <p>
            <br /><br>
          </p>
        </div>
      </div>
    </div>

    <div>
      <Footer />
    </div>
  </div>
</template>

<script>
import Menu from "../../common/header/private/menu.vue";
import Footer from "../../common/footer/footer.vue";

export default {
  data: () => ({
    database_selected: "",
    database_names: ["HadaBeer", "Chocolate"],
    model_selected: "",
    config_parameters : {
      model_name: "",
      learning_rate: "learning_rate",
      target_selected: "",
      target: ["Graduation", "intensity"],
      epsilon: "Epsilon",
      epochs: "Epochs",
      optimizer_selected: "",
      optimizer: ["Adam", "Adamax", "SGD", "RMSprop", "Adadelta", "Adagrad", "Adamax", "Nadam", "Ftrl"],
      loss_selected: "",
      loss: ["binary_crossentropy", "sparse_categorical_crossentropy"],
      metrics: ["acc"],
      batch_size: "Batch size",
      decay: "Decay",
      train_size: "Train size",
      validation_size: "Validation size"
    },
    model_names: ["model_1", "model_2"],
    layers: [
      {
        name: "Dense",
        parameters: ["units"],
        activation: ""
      },
      {
        name: "Dropout",
        parameters: ["rate"],
        activation: ""
      },
      {
        name: "Softmax",
        parameters: [],
        activation: ""
      },
      {
        name: "MaxPool1D",
        parameters: ["pool_size"],
        activation: ""
      },
      {
        name: "AveragePool1D",
        parameters: ["pool_size"],
        activation: ""
      }
    ]
  }),
  methods: {
    getImgNN() {
      return require("/src/assets/private/training/neural_net.png");
    }
  },
  components: {
    Menu,
    Footer,
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.training-page {
  background-color: #deeaee;
  height: 100%;
  min-height: 100vh; /* will cover the 100% of viewport */
  overflow: hidden;
  display: block;
  position: relative;
  padding-bottom: 10vw; /* height of your footer */
}

h1, h2, h3 {
  margin-top: 4vw;
  margin-bottom: 3vw;
  text-align: center;
}

h3 {
  width: 100%;
  padding-left: 7vw;
  padding-top: 0vw;
}

.delete_button {
  background-color: red !important;
  margin-top: 1vw;
  margin-left: 1vw;
}

label {
  width: 100% !important;
}

#train_menu {
  width: 100%;
}

.train_form {
  width: 95%;
  margin-top: 2vw;
}

.combo_select_db {
  width: 45%;
  float: left;
}

.combo_select_model {
  width: 45%;
  float: right;
}

.train_button {
  margin: 0.75vw;
}

#config_admin {
  width: 100%;
}

#config_parameters {
  width: 60%;
  float: left;
}

.combo_parameters_left, #model_name_input, .target_input, #epochs_input, .loss_input, #batch_size_input, #train_size_input {
  width:48%;
  float: left;
  margin-right: 1vw;
}


.combo_parameters_right, #learning_rate_input, #epsilon_input, .optimizer_input, .metrics_input, #decay_input, #validation_size_input {
  width: 48%;
  float: right;
}

.config_parameters_form {
  width: 92%;
}

#admin_models {
  width: 40%;
  float: right;
}

#design_palette {
  margin-top: auto !important;
}

#palette {
  width: 100%;
}

#layers {
  width: 100%;
  float: right;
}

#layers_names {
  float: right;
  padding: 0%;
  max-height: fit-content;
}

.border_rect {
  width: 90%;
  margin-left: 2vw;
}

.palette_form {
  width: 20%;
  padding: 0vw;
}

.cf_button {
  width: 100%;
  margin: 0;
  float: left;
}

/*******************  Layers image ****************************/

.container {
  max-width: fit-content;
  margin: inherit;
  float: left;
  position: relative;
  height: auto;
  display: block;
}

.layer {
  padding:5px;
  margin:5px;
  margin-left: 0%;
  margin-right: 0%;
  background-color: #EE3744;
  border: solid 1px #EE3744;
  color: #deeaee;
  border-radius: 0.75em;
  text-align: justify;
  text-justify: inter-word;
  font-size: 1.5vw;
  width: fit-content;
}

</style>
