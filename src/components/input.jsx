import React, { useState, useEffect } from "react";
import { ScaleIcon } from '@heroicons/react/solid'
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';

const SignIn = () => {
    const [quote, setQuote] = useState("");
    const [party, setParty] = useState("");
    
    function updateQuote(e)
    {
        setQuote(e.target.value);
        console.log(e.target.value);
    }
    function evaluate(e)
    {
      e.preventDefault();
      e.stopPropagation();
      if(party === "Republican")
        setParty("Liberal");
      else
        setParty("Republican");
    }

  return (
    <>
      <div className="min-h-full flex align-middle items-center justify-center py-12 px-4 sm:px-6 lg:px-8" style={{height:"100vh"}}>
        <Container className="max-w-md w-full space-y-8 align-middle">
          <Row>
            <h1>{party}</h1>
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
          <form className="mt-8 space-y-6" onSubmit={(e) => evaluate(e)}>
          <Row>
            <div className="rounded-md shadow-sm -space-y-px">
              <div>
                <label htmlFor="quote1" className="sr-only">
                  Email address
                </label>
                <input
                  id="Quote"
                  name="quote1"
                  type="text"
                  onChange = { e => updateQuote(e)}
                  required
                  className="appearance-none rounded-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-t-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                  placeholder="Paste your quote here"
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
    </>
  )
}


export default SignIn;