import { ScaleIcon } from '@heroicons/react/solid';
import axios from "axios";
import React, { useState } from "react";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
const SignIn = () => {
    const url = "/api/word";
    const [quote, setQuote] = useState("");

    const handleChange = (e) => {
      const regex = /[^a-z0-9\s]/igm;
      let temp = e.target.value.replaceAll(regex, "");
      setQuote(temp);
    };

    const submitStudent = (e) => {
      e.preventDefault();
      e.stopPropagation();
      console.log(quote);

      axios
      .post(url + "/party/" + quote)
      .then((res) => {
        console.log(res);
        console.log(res.data);
        console.log(res.data["Liberal"]);
        let r_head = document.getElementById("result_head");
        let r_body = document.getElementById("result_body");
        if(res.data["Liberal"] > res.data["Republican"]){
            r_head.textContent = "Liberal"
        }
        else if(res.data["Liberal"] < res.data["Republican"]){
            r_head.textContent = "Republican"
        }
        else
        {
            r_head.textContent = "Tie"
        }
        document.getElementById("Word_stat").textContent = "Word Count : " + res.data["Word_Count"];
        document.getElementById("Lib_stat").textContent = "Liberal : " + res.data["Liberal"];
        document.getElementById("Rep_stat").textContent = "Republican : " + res.data["Republican"];
      })
      .catch((err) => {
        console.log(err);
      });
    }
    
return (
      <div className="min-h-full flex align-middle items-center justify-center py-12 px-4 sm:px-6 lg:px-8" style={{height:"100vh"}}>
        <Container className="max-w-md w-full space-y-8 align-middle">
            <Row>
                <h1 id="result_head"  className="text-center text-3xl font-extrabold text-indigo-900"></h1>
                <div id="result_body" className="mt-3 font-medium text-gray-900">
                    <p id="Word_stat" ></p>
                    <p id="Lib_stat"></p>
                    <p id="Rep_stat"></p>
                </div>
            </Row>
          <Row>
            <img
              className="mx-auto h-12 w-auto"
              src="https://tailwindui.com/img/logos/workflow-mark-indigo-600.svg"
              alt="Workflow"
            />
            <h2 className="mt-6 text-center text-3xl font-extrabold text-gray-900">PolitiPredict</h2>
            <p className="mt-2 text-center text-sm text-gray-600">
              <p href="#" className="font-medium text-indigo-600 hover:text-indigo-500">
                Gauge politicial bias of statements with AI
              </p>
            </p>
          </Row>
          <form id="studentform" className="mt-8 space-y-6" onSubmit = { e => submitStudent(e)}>
          <Row>
            <div className="rounded-md shadow-sm -space-y-px">
              <div>
                <label htmlFor="quote" className="sr-only">
                  quote
                </label>
                <input
                  id="Quote"
                  name="quote"
                  type="text"
                  onChange = { e => handleChange(e)}
                  required
                  className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                  placeholder="Paste quote here"
                />
              </div>
            </div>
            </Row>
            <Row>
            <div>
              <button
                type="submit"
                className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                <span className="absolute left-0 inset-y-0 flex items-center pl-3">
                  <ScaleIcon className="h-5 w-5 text-indigo-500 group-hover:text-indigo-400" aria-hidden="true" />
                </span>
                Evaluate
              </button>
            </div>
            </Row>
          </form>
        </Container>
      </div>
  )
}


export default SignIn;