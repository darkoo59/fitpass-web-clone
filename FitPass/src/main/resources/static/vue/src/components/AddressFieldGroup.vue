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

      <input type="hidden"
             @input="$emit('update:lat', $event.target.value)"
             :value="lat"/>

      <input type="hidden"
             @input="$emit('update:lng', $event.target.value)"
             :value="lng"/>

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
      type: String,
      default: ''
    },
    lng: {
      type: String,
      default: ''
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
      autocomplete.setComponentRestrictions({
        country: ["rs"],
      });

      google.maps.event.addListener(autocomplete, "place_changed", () => {
        const mapping = {
          route: "update:street",
          street_number: "update:number",
          locality: "update:place",
          postal_code: "update:zipCode",
          lat: "update:lat",
          lng: "update:lng"
        }

        for (const type in mapping) {
          context.emit(mapping[type], "");
        }

        let place = {
          address_components: [],
          geometry: [],
          ...autocomplete.getPlace()
        }

        place.address_components.forEach((component) => {
          component.types.forEach((type) => {
            if (mapping.hasOwnProperty(type)) {
              context.emit(mapping[type], component.long_name);
            }
          });
        });

          context.emit(mapping["lat"], place.geometry.location.lat())
          context.emit(mapping["lng"], place.geometry.location.lng())
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