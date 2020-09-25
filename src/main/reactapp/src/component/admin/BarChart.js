import React from "react";
import {Bar} from "react-chartjs-2"

export default function Barchart(props) {
    const data = {
        labels: Object.keys(props.data),
        datasets: [
            {
                display: true,
                label: "Katılımcı Sayısı",
                data: Object.values(props.data),
                borderColor: "blue",
                backgroundColor: "midnightBlue",
                pointBackgroundColor: "green",
                pointBorderColor: "purple"
            }
        ]
    }

    const options = {
        title: {
            display: true,
            text: "Etkinliklerin Katılımcı Sayıları"
        },
        scales: {
            display: props.display,
            yAxes: [
                {
                    ticks: {
                        min: 0,
                        max: Math.max(...Object.values(props.data)),
                        stepSize: Math.floor(Math.max(...Object.values(props.data)) / Object.values(props.data).length)
                    }
                }
            ]
        }
    }

    return props.display === true ? <Bar data={data} options={options}/> : <div/>
}