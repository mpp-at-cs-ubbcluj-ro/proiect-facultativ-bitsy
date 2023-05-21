using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;

namespace HelpingHands.Data
{
    internal class Eveniment
    {
        [JsonProperty("id")]
        public int Id { get; set; }
        [JsonProperty("name")]
        public string Name { get; set; }
        [JsonProperty("description")]
        public string Description { get; set; }
        [JsonProperty("startDate")]
        public DateTime StartDate { get; set; }
        [JsonProperty("endDate")]
        public DateTime EndDate { get; set; }
        [JsonProperty("location")]
        public string Location { get; set; }
        [JsonProperty("interests")]
        public string[] Interests { get; set; }
        [JsonProperty("initiatorId")]
        public int InitiatorId { get; set; }
        [JsonProperty("status")]
        public string Status { get; set; }
    }
}