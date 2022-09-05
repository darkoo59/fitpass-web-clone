<template>
  <div class="grid grid-cols-3 gap-5">

    <label class="col-span-2">
      Street
      <input type="text"
             @input="$emit('update:street', $event.target.value)"
             :value="street"
             ref="streetRef"
             placeholder="Street"/>
    </label>

    <label class="col-span-2">
      Number
      <input type="text"
             @input="$emit('update:number', $event.target.value)"
             :value="number"
             placeholder="Number"/>

      <label class="col-span-2">
        Place
        <input type="text"
               @input="$emit('update:place', $event.target.value)"
               :value="place"
               placeholder="Place"/>
      </label>

      <label class="col-span-2">
        Zip code
        <input type="text"
               @input="$emit('update:zipCode', $event.target.value)"
               :value="zipCode"
               placeholder="Zip code"/>
      </label>

    </label>
  </div>
</template>

<script>
import {onMounted, ref, onUnmounted} from "vue";
import useScript from "@/composables/useScript";

export default {
  props: {
    street: {
      type: String,
      default: ''
    },
    number: {
      type: String,
      default: ''
    },
    place: {
      type: String,
      default: ''
    },
    zipCode: {
      type: String,
      default: ''
    },
    lat: {
      type: Number,
      default: 0.0
    },
    lng: {
      type: Number,
      default: 0.0
    }
  },
  setup(props, context) {
    const streetRef = ref();
    let autocomplete;

    onMounted(async() => {
      await useScript("https://maps.googleapis.com/maps/api/js?key=AIzaSyBx8GVH-2qbiuswKuukDTH5bIbh9XZwSoI&libraries=places")
      autocomplete = new google.maps.places.Autocomplete(streetRef.value, {
        types: ["address"],
        fields: ["address_components", "geometry"]
      })

      google.maps.event.addListener(autocomplete, "place_changed", () => {
        const mapping = {
          route: "update:street",
          street_number: "update:number",
          locality: "update:place",
          postal_code: "update:zipCode",
          lat: "update:lat",
          lng: "update:lng"
        }

        for(const type in mapping) {
          context.emit(mapping[type], "");
        }

        let place = {
          address_components: [],
          ...autocomplete.getPlace()
        }

        place.address_components.forEach((component) => {
          component.types.forEach((type) => {
            if(mapping.hasOwnProperty(type)){
              context.emit(mapping[type], component.long_name);
            }
          });
        });
      });
    });

    onUnmounted(() => {
      if(autocomplete) {
        google.maps.event.clearInstanceListeners(autocomplete);
      }
    });

    return {streetRef};
  }
}
</script>