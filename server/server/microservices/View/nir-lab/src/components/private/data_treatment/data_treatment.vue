<template>
  <div class="data_treatment-page">
    <div>
      <Menu page="/data_treatment" name="brais" />
    </div>
    <h1>Data Treatment</h1>

    <!-- Selects and geners -->
    <div id="gen_sel">
      <!-- Generate new database div -->
      <div id="gen_new_db">
        <form class="vue-form gen_db_form" @submit.prevent="generateNewDB">
          <fieldset>
            <label>Generate a database!</label>
            <input
              type="text"
              placeholder="Database name"
              name="database_to_generate_name"
              id="database_to_generate_name"
              required
              v-model="database_to_generate_name"
            />
            <v-combobox
              class="combo_config"
              required
              v-model="configuration_file_selected"
              :items="configuration_files"
              placeholder="Select a configuration file"
            >
            </v-combobox>
            <input
              class="action_button"
              type="submit"
              value="Generate new database"
            />
          </fieldset>
        </form>
      </div>

      <!-- Add / select a database -->
      <div id="select_db">
        <form class="vue-form sel_db_form" @submit.prevent="deleteDB">
          <fieldset>
            <input
              class="delete_button"
              type="submit"
              value="Delete the database"
            />
            <label>Select a database</label>
            <v-combobox
              class="combo_database"
              required
              v-model="database_selected"
              :items="databases_names"
              placeholder="Select a database"
            >
            </v-combobox>
            <div class="drop-zone">
              <b-img
                class="db_image"
                :src="getImgDb()"
                alt="Database image"
              ></b-img>
            </div>
          </fieldset>
        </form>
      </div>
    </div>

    <h2>Design Palette</h2>
    <!-- Desing Palette -->
    <div id="design_palette">
      <div id="palette">
        <form
          class="vue-form palette_form"
          @submit.prevent="generateNewConfigFile"
        >
          <fieldset>
            <input
              class="cf_button"
              type="submit"
              value="Generate config file"
            />
            <input
              type="text"
              placeholder="Configuration file name"
              name="configuration_file_to_generate_name"
              id="configuration_file_to_generate_name"
              required
              v-model="configuration_file_to_generate_name"
            />
          </fieldset>
        </form>
        <div class="border_rect">
          <p>
            <br /><br>
          </p>
        </div>
      </div>
      <div id="variables">
        <h3>Operations</h3>
        <h3 id="var_title">Variables</h3>
        <h3 id="output_title">Output</h3>

        <div class="border_rect" id="var_names">
          <p>
            <br /><br>
          </p>
        </div>

        <div class="border_rect" id="operation_names">
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
    // for generating new db parting from files
    database_to_generate_name: "",
    configuration_file_selected: "",
    configuration_files: ["config_file_1", "config_file_2", "config_file_3"],
    // for delete // generate databases
    database_selected: "",
    databases_names: ["HadaBeer", "Chocolate", "Tejidos"],
    // for generate config -- DESINGN PALETTE
    configuration_file_to_generate_name: "",
    variables: [
      {
        name: "intensity",
        is_output: false,
      },
      {
        name: "absorbance",
        is_output: false,
      },
      {
        name: "reflectance",
        is_output: false,
      },
    ],
    operations: [
      {
        name: "sum",
        num_parameters: 2,
      },
      {
        name: "subtract",
        num_parameters: 2,
      },
      {
        name: "multiply",
        num_parameters: 2,
      },
      {
        name: "divide",
        num_parameters: 2,
      },
      {
        name: "module",
        num_parameters: 2,
      },
      {
        name: "whole_divide",
        num_parameters: 2,
      },
      {
        name: "square_root",
        num_parameters: 1,
      },
      {
        name: "raise_x",
        num_parameters: 2,
      },
      {
        name: "standard_deviation",
        num_parameters: 1,
      },
      {
        name: "mean",
        num_parameters: 1,
      },
      {
        name: "median",
        num_parameters: 1,
      },
      {
        name: "mode",
        num_parameters: 1,
      },
      {
        name: "sum_all",
        num_parameters: 1,
      },
      {
        name: "minimum",
        num_parameters: 1,
      },
      {
        name: "maximum",
        num_parameters: 1,
      },
      {
        name: "min_max_normalization",
        num_parameters: 1,
      },
      {
        name: "normalize",
        num_parameters: 1,
      },
      {
        name: "variance",
        num_parameters: 1,
      },
      {
        name: "max_abs_normalization",
        num_parameters: 1,
      },
      {
        name: "value_at",
        num_parameters: 2,
      },
    ],
  }),
  methods: {
    getImgDb() {
      return require("/src/assets/private/data_treatment/data_base.png");
    },
    generateNewDB() {
      return null;
    },
    deleteDB() {
      return null;
    },
  },
  components: {
    Menu,
    Footer,
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
.data_treatment-page {
  background-color: #deeaee;
  height: 100%;
  min-height: 100vh; /* will cover the 100% of viewport */
  overflow: hidden;
  display: block;
  position: relative;
  padding-bottom: 10vw; /* height of your footer */
}

#gen_sel {
  margin-top: 1vw;
  display: flex;
  padding: 0vw;
}

#gen_new_db {
  float: left;
  width: 50%;
}

.gen_db_form,
.sel_db_form {
  margin: 2vw;
  width: 90%;
  border-radius: 0.75vw;
}

#select_db {
  float: right;
  width: 50%;
  height: 100%;
}

.vue-form div {
  margin: 2px 0 !important;
}

h1 {
  margin-top: 4vw;
  margin-bottom: 3vw;
  text-align: center;
}

h2 {
  margin-top: 5vw;
  text-align: center;
}

.delete_button {
  background-color: red !important;
  margin-top: -1.5vw;
}

#design_palete {
  margin-top: auto !important;
}

.drop-zone {
  border: solid 1px #ee3744;
  padding: 10px;
  border-radius: 0.75vw;
  height: inherit;
  text-align: center;
}

.db_image {
  width: 10%;
}

/* Design palette */
#palette {
  float: left;
  margin: 0 2vw;
  width: 50%;
}

.palette_form {
  width: 100%;
  border-radius: 0.75vw;
}

.cf_button {
  width: 35%;
  border: 0vw;
  font-size: 10pt;
}

#configuration_file_to_generate_name {
  width: 60%;
  padding: 0.55vw;
}

h3 {
  padding-top: 5vw;
  float: right;
  margin-right: 3vw;
}

#var_title {
  margin-right: 7.5vw;
}

#output_title {
  margin-right: 7.5vw;
}

#variables {
  float: right;
  padding: 0%;
}

#var_names {
  float: left;
  width: 66%;
  margin-left: -2vw;
  margin-top: 4vw;
}

#operation_names {
  float: right;
  width: 34%;
  margin-left: -2vw;
  margin-right: 1.5vw;
  margin-top: 4vw;
}
</style>
